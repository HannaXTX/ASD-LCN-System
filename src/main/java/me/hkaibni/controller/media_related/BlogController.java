package me.hkaibni.controller.media_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.BlogCreationDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.model.media.Blog;
import me.hkaibni.model.media.News;
import me.hkaibni.service.media.BlogService;
import me.hkaibni.utils.ResponseUtil;

import java.time.LocalDateTime;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogController {

    @Inject
    BlogService blogService;


    // GET ALL BLOGS
    @GET
    @RolesAllowed({"USER","ADMIN"})
    public Response getBlogs() {
        return ResponseUtil.ok("Blogs retrieved successfully", blogService.getAllBlogs());
    }


    // GET BLOG BY ID
    @GET
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response getBlog(@PathParam("id") String id) {
        Blog blog = blogService.getBlog(id);
        if (blog == null){
            return ResponseUtil.notFound("Blog not found");
        }
        return ResponseUtil.ok("Blog retrieved successfully",blog);
    }


    // CREATE BLOG
    @POST
    @Path("/create")
    @RolesAllowed({"USER","ADMIN"})
    public Response createBlog(BlogCreationDTO dto) {
        return ResponseUtil.created("Blog created successfully", blogService.createBlog(dto));
    }


    // UPDATE BLOG
    @PUT
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response updateBlog(@PathParam("id") String id, BlogCreationDTO dto) {

        Blog blog = blogService.updateBlog(id, dto);

        if (blog == null)
            return ResponseUtil.notFound("Blog not found");

        return ResponseUtil.ok("Blog updated successfully", blog);
    }

    // DELETE BLOG
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response deleteBlog(@PathParam("id") String id) {
        Blog blog = blogService.getBlog(id);
        if (blog == null){
            return ResponseUtil.notFound("Blog not found");
        }
        blogService.deleteBlog(id);

        return ResponseUtil.ok("Blog Deleted Successfully",null);
    }
}
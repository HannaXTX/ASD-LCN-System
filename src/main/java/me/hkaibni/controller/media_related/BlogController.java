package me.hkaibni.controller.media_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.entity_dto.BlogDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.service.media.BlogService;

import java.time.LocalDateTime;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogController {

    @Inject
    BlogService blogService;


    // GET ALL BLOGS
    @GET
    @RolesAllowed("USER")
    public Response getBlogs() {

        return Response.ok(
                new ApiResponse(
                        200,
                        "Blogs retrieved successfully",
                        blogService.getAllBlogs(),
                        LocalDateTime.now()
                )
        ).build();
    }


    // GET BLOG BY ID
    @GET
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response getBlog(
            @PathParam("id") String id
    ) {

        return Response.ok(
                new ApiResponse(
                        200,
                        "Blog retrieved successfully",
                        blogService.getBlog(id),
                        LocalDateTime.now()
                )
        ).build();
    }


    // CREATE BLOG
    @POST
    @RolesAllowed("USER")
    public Response createBlog(BlogDTO dto) {

        return Response
                .status(Response.Status.CREATED)
                .entity(
                        new ApiResponse(
                                201,
                                "Blog created successfully",
                                blogService.createBlog(dto),
                                LocalDateTime.now()
                        )
                )
                .build();
    }


    // UPDATE BLOG
    @PUT
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response updateBlog(
            @PathParam("id") String id,
            BlogDTO dto
    ) {

        return Response.ok(
                new ApiResponse(
                        200,
                        "Blog updated successfully",
                        blogService.updateBlog(id, dto),
                        LocalDateTime.now()
                )
        ).build();
    }


    // DELETE BLOG
    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response deleteBlog(
            @PathParam("id") String id
    ) {

        blogService.deleteBlog(id);

        return Response.ok(
                new ApiResponse(
                        200,
                        "Blog deleted successfully",
                        null,
                        LocalDateTime.now()
                )
        ).build();
    }
}
package me.hkaibni.controller.media_related;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.NewsDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.model.media.Blog;
import me.hkaibni.model.media.News;
import me.hkaibni.service.media.NewsService;
import me.hkaibni.utils.ResponseUtil;

import java.time.LocalDateTime;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NewsController {

    @Inject
    NewsService newsService;

    @GET
    @RolesAllowed({"USER","ADMIN"})
    public Response getAllNews() {
        return ResponseUtil.ok("News retrieved successfully", newsService.getAllNews());
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response getNewsById(@PathParam("id") String id) {

        News news = newsService.getNews(id);
        if (news == null){
            return ResponseUtil.notFound("News not found");
        }
        return ResponseUtil.ok("News retrieved Successfully",news);

    }

    // CREATE NEWS
    @Path("/create")
    @POST
    @RolesAllowed({"USER","ADMIN"})
    public Response createNews(NewsDTO dto) {
        return ResponseUtil.ok("News created successfully",newsService.createNews(dto));
    }

    // UPDATE NEWS
    @PUT
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response updateNews(@PathParam("id") String id, NewsDTO dto) {
        News news = newsService.updateNews(id, dto);

        if (news == null)
            return ResponseUtil.notFound("Blog not found");

        return ResponseUtil.ok("Blog updated successfully", news);

    }

    // DELETE NEWS
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response deleteNews(@PathParam("id") String id) {
        News news = newsService.getNews(id);
        if (news == null){
            return ResponseUtil.notFound("News not found");
        }
        newsService.deleteNews(id);

        return ResponseUtil.ok("News deleted successfully",null);
    }



//    @Path("/attached")
//    @GET
//    @PermitAll
//    public Response attachFiles(NewsDTO dto) {
//
//        return Response
//                .status(Response.Status.CREATED)
//                .entity(
//                        new ApiResponse(
//                                201,
//                                "News created successfully",
//                                newsService.createNews(dto),
//                                LocalDateTime.now()
//                        )
//                )
//                .build();
//    }



}
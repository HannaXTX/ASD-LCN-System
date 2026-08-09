package me.hkaibni.controller.media_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.entity_dto.NewsDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.service.media.NewsService;

import java.time.LocalDateTime;
import java.util.UUID;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NewsController {

    @Inject
    NewsService newsService;

    // GET ALL NEWS
    @GET
    @RolesAllowed("USER")
    public Response getAllNews() {

        return Response.ok(
                new ApiResponse(
                        200,
                        "News retrieved successfully",
                        newsService.getAllNews(),
                        LocalDateTime.now()
                )
        ).build();
    }

    // GET NEWS BY ID
    @GET
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response getNews(
            @PathParam("id") UUID id
    ) {

        return Response.ok(
                new ApiResponse(
                        200,
                        "News retrieved successfully",
                        newsService.getNews(id),
                        LocalDateTime.now()
                )
        ).build();
    }

    // CREATE NEWS
    @POST
    @RolesAllowed("USER")
    public Response createNews(NewsDTO dto) {

        return Response
                .status(Response.Status.CREATED)
                .entity(
                        new ApiResponse(
                                201,
                                "News created successfully",
                                newsService.createNews(dto),
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    // UPDATE NEWS
    @PUT
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response updateNews(
            @PathParam("id") UUID id,
            NewsDTO dto
    ) {

        return Response.ok(
                new ApiResponse(
                        200,
                        "News updated successfully",
                        newsService.updateNews(id, dto),
                        LocalDateTime.now()
                )
        ).build();
    }

    // DELETE NEWS
    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response deleteNews(
            @PathParam("id") UUID id
    ) {

        newsService.deleteNews(id);

        return Response.ok(
                new ApiResponse(
                        200,
                        "News deleted successfully",
                        null,
                        LocalDateTime.now()
                )
        ).build();
    }
}
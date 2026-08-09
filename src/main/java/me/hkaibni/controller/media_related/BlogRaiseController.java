package me.hkaibni.controller.media_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.service.media.BlogRaiseService;

import java.time.LocalDateTime;


@Path("/blogs/{blogId}/raises")
@Produces(MediaType.APPLICATION_JSON)
public class BlogRaiseController {

    @Inject
    BlogRaiseService blogRaiseService;


    @POST
    @RolesAllowed({"USER", "ADMIN"})
    public Response raiseBlog(
            @PathParam("blogId") String blogId
    ) {

        return Response.status(
                        Response.Status.CREATED
                )
                .entity(
                        new ApiResponse(
                                201,
                                "Blog raised successfully",
                                blogRaiseService
                                        .raiseBlog(blogId),
                                LocalDateTime.now()
                        )
                )
                .build();
    }


    @DELETE
    @RolesAllowed({"USER", "ADMIN"})
    public Response removeRaise(
            @PathParam("blogId") String blogId
    ) {

        return Response.ok(
                new ApiResponse(
                        200,
                        "Blog raise removed successfully",
                        blogRaiseService
                                .removeRaise(blogId),
                        LocalDateTime.now()
                )
        ).build();
    }


    @GET
    @RolesAllowed({"USER", "ADMIN"})
    public Response getRaiseStatus(
            @PathParam("blogId") String blogId
    ) {

        return Response.ok(
                new ApiResponse(
                        200,
                        "Blog raise status retrieved successfully",
                        blogRaiseService
                                .getRaiseStatus(blogId),
                        LocalDateTime.now()
                )
        ).build();
    }
}
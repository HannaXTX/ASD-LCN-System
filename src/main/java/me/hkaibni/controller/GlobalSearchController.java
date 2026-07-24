package me.hkaibni.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.ApiResponse;
import me.hkaibni.dto.SearchResultDTO;
import me.hkaibni.service.GlobalSearchService;

import java.util.Date;
import java.util.List;

@Path("/search")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON)
public class GlobalSearchController {

    @Inject
    GlobalSearchService globalSearchService;

    @GET
    public Response search(@QueryParam("q") String query) {

        if (query == null || query.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            400,
                            "Search query is required",
                            null,
                            new Date(System.currentTimeMillis())
                    ))
                    .build();
        }

        List<SearchResultDTO> results =
                globalSearchService.search(query.trim());

        return Response.ok(
                new ApiResponse(
                        200,
                        results.isEmpty()
                                ? "No results found"
                                : "Search completed successfully",
                        results,  new Date(System.currentTimeMillis())
                )
        ).build();
    }
}
package me.hkaibni.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.entity_dto.FamilyDTO;
import me.hkaibni.dto.response.FamilyTreeResponse;
import me.hkaibni.dto.search.FamilySearchDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.userdata.User;
import me.hkaibni.service.FamilyService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Path("/family")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FamilyController {

    @Inject
    FamilyService familyServ;

    @GET
//    @RolesAllowed("ADMIN")
    public Response getAllFamilies() {

        List<Family> families = familyServ.getAllFamilies();

        return Response.ok(
                new ApiResponse(
                        200,
                        "Families retrieved successfully",
                        families,
                        LocalDateTime.now()
                )
        ).build();
    }


    @POST
    @RolesAllowed("ADMIN")
    public Response createFamily(FamilyDTO dto) {

        int operation = familyServ.createFamily(dto);

        if (operation == 0) {
            Family family = familyServ.getFamilyByNameEn(dto.getNameEn());

            return Response.status(Response.Status.CREATED)
                    .entity(
                            new ApiResponse(
                                    201,
                                    "Family created successfully",
                                    family,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        if (operation == 1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(
                            new ApiResponse(
                                    400,
                                    "Family data is required",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        if (operation == 2) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(
                            new ApiResponse(
                                    409,
                                    "A family with this SSN already exists",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                "A family with this name already exists",
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    
    @GET
    @Path("/id/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response getFamilyById(@PathParam("id") UUID id) {

        Family family = familyServ.getFamilyById(id);

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family retrieved successfully",
                        family,
                        LocalDateTime.now()
                )
        ).build();
    }


    @GET
    @Path("/name/{familyName}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response getFamilyByName(
            @PathParam("familyName") String familyName
    ) {

        Family family = familyServ.getFamilyByNameEn(familyName);

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family retrieved successfully",
                        family,
                        LocalDateTime.now()
                )
        ).build();
    }

    @GET
    @Path("/search")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchFamilies(FamilySearchDTO dto) {
        List<Family> results;
        if (dto.hasNoCriteria()) {
            results = familyServ.searchFamilies(dto.getValue(),dto.getPage(),dto.getPageSize());
        }
        else {
            results = familyServ.searchFamilies(dto);
        }
        return Response.ok(
                new ApiResponse(
                        200,
                        results.isEmpty()
                                ? "No users found"
                                : "User search completed successfully",
                        results,
                        LocalDateTime.now()
                )
        ).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateFamily(
            @PathParam("id") UUID id,
            FamilyDTO dto
    ) {

        int operation = familyServ.updateFamily(id, dto);

        if (operation == 0) {
            return Response.ok(
                    new ApiResponse(
                            200,
                            "Family updated successfully",
                            familyServ.getFamilyById(id),
                            LocalDateTime.now()
                    )
            ).build();
        }

        if (operation == 1) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        if (operation == 2) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(
                            new ApiResponse(
                                    409,
                                    "Family name already exists",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                "Family name already exists",
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    @DELETE
    @Path("/id/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteFamilyById(
            @PathParam("id") UUID id
    ) {

        if (!familyServ.deleteFamilyById(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family deleted successfully",
                        null,
                        LocalDateTime.now()
                )
        ).build();
    }
    @GET
    @Path("/{id}/tree")
    @PermitAll
    public Response getFamilyTree(@PathParam("id") UUID id) {

        Family family = familyServ.getFamilyById(id);

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        FamilyTreeResponse familyTree =
                familyServ.buildTree(id);

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family tree retrieved successfully",
                        familyTree,
                        LocalDateTime.now()
                )
        ).build();
    }

}
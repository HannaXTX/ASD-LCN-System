package me.hkaibni.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.ApiResponse;
import me.hkaibni.dto.FamilyDTO;
import me.hkaibni.model.Family;
import me.hkaibni.service.FamilyService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path("/family")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FamilyController {

    @Inject
    FamilyService familyServ;

    @POST
    @RolesAllowed("ADMIN")
    public Response createFamily(FamilyDTO dto) {

        int operation = familyServ.createFamily(dto);

        if (operation == 0) {
            Family family = familyServ.getFamilyBySSN(dto.getSSN());

            return Response.status(Response.Status.CREATED)
                    .entity(
                            new ApiResponse(
                                    201,
                                    "Family created successfully",
                                    family,
                                    new Date(System.currentTimeMillis())
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
                                    new Date(System.currentTimeMillis())
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
                                    new Date(System.currentTimeMillis())
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
                                new Date(System.currentTimeMillis())
                        )
                )
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public Response getAllFamilies() {

        List<Family> families = familyServ.getAllFamilies();

        return Response.ok(
                new ApiResponse(
                        200,
                        "Families retrieved successfully",
                        families,
                        new Date(System.currentTimeMillis())
                )
        ).build();
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
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family retrieved successfully",
                        family,
                        new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @GET
    @Path("/ssn/{SSN}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response getFamilyBySSN(
            @PathParam("SSN") String SSN
    ) {

        Family family = familyServ.getFamilyBySSN(SSN);

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family retrieved successfully",
                        family,
                        new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @GET
    @Path("/name/{familyName}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response getFamilyByName(
            @PathParam("familyName") String familyName
    ) {

        Family family = familyServ.getFamilyByName(familyName);

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family retrieved successfully",
                        family,
                        new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @GET
    @Path("/search")
    @RolesAllowed({"ADMIN", "USER"})
    public Response searchFamilies(
            @QueryParam("name") String familyName
    ) {

        if (familyName == null || familyName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(
                            new ApiResponse(
                                    400,
                                    "Family name is required",
                                    null,
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        List<Family> families = familyServ.searchFamilies(familyName);

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family search completed successfully",
                        families,
                        new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @PUT
    @Path("/{SSN}")
    @RolesAllowed("ADMIN")
    public Response updateFamily(
            @PathParam("SSN") String SSN,
            FamilyDTO dto
    ) {

        int operation = familyServ.updateFamily(SSN, dto);

        if (operation == 0) {
            return Response.ok(
                    new ApiResponse(
                            200,
                            "Family updated successfully",
                            familyServ.getFamilyBySSN(dto.getSSN()),
                            new Date(System.currentTimeMillis())
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
                                    new Date(System.currentTimeMillis())
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
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                "Family SSN already exists",
                                null,
                                new Date(System.currentTimeMillis())
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
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family deleted successfully",
                        null,
                        new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @DELETE
    @Path("/ssn/{SSN}")
    @RolesAllowed("ADMIN")
    public Response deleteFamilyBySSN(
            @PathParam("SSN") String SSN
    ) {

        if (!familyServ.deleteFamilyBySSN(SSN)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Family not found",
                                    null,
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Family deleted successfully",
                        null,
                        new Date(System.currentTimeMillis())
                )
        ).build();
    }
}
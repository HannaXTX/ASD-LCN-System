package me.hkaibni.controller.family_related;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.entity_dto.TreeRelationshipDTO;
import me.hkaibni.dto.response.ApiResponse;

import me.hkaibni.service.family.TreeRelationshipService;
import me.hkaibni.service.status.RelationshipCreationStatus;

import java.time.LocalDateTime;
@Path("/relationships")
public class TreeRelationshipController {
    @Inject
    TreeRelationshipService relationshipService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRelationship(TreeRelationshipDTO dto) {

        RelationshipCreationStatus result =
                relationshipService.createRelationship(dto);

        LocalDateTime now = LocalDateTime.now();

        return switch (result) {

            case SUCCESS -> buildResponse(
                    Response.Status.CREATED,
                    "Relationship created successfully",
                    now
            );

            case INVALID_REQUEST -> buildResponse(
                    Response.Status.BAD_REQUEST,
                    "Family, both people, and relationship type are required",
                    now
            );

            case SELF_RELATIONSHIP -> buildResponse(
                    Response.Status.BAD_REQUEST,
                    "A person cannot have a relationship with themselves",
                    now
            );

            case FAMILY_NOT_FOUND -> buildResponse(
                    Response.Status.NOT_FOUND,
                    "Family not found",
                    now
            );

            case PERSON_A_NOT_FOUND -> buildResponse(
                    Response.Status.NOT_FOUND,
                    "Person A not found",
                    now
            );

            case PERSON_B_NOT_FOUND -> buildResponse(
                    Response.Status.NOT_FOUND,
                    "Person B not found",
                    now
            );

            case PERSON_A_NOT_IN_FAMILY -> buildResponse(
                    Response.Status.CONFLICT,
                    "Person A is not a member of this family",
                    now
            );

            case PERSON_B_NOT_IN_FAMILY -> buildResponse(
                    Response.Status.CONFLICT,
                    "Person B is not a member of this family",
                    now
            );

            case ALREADY_EXISTS -> buildResponse(
                    Response.Status.CONFLICT,
                    "This relationship already exists",
                    now
            );
        };
    }

    private Response buildResponse(
            Response.Status status,
            String message,
            LocalDateTime timestamp
    ) {
        return Response.status(status)
                .entity(new ApiResponse(
                        status.getStatusCode(),
                        message,
                        null,
                        timestamp
                ))
                .build();
    }
}

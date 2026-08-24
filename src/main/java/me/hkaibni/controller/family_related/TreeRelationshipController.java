package me.hkaibni.controller.family_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.TreeRelationshipDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.family.Person;
import me.hkaibni.service.family.TreeRelationshipService;
import me.hkaibni.utils.ResponseUtil;

@Path("/relationships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TreeRelationshipController {

    @Inject
    TreeRelationshipService relationshipService;


    @POST
    @RolesAllowed({"USER","ADMIN"})
    public Response createRelationship(TreeRelationshipDTO dto) {

        if (dto == null || dto.getFamilyId() == null || dto.getPersonAId() == null || dto.getPersonBId() == null || dto.getType() == null) {
            return ResponseUtil.badRequest("Family, both people, and relationship type are required");
        }

        if (dto.getPersonAId().equals(dto.getPersonBId())) {
            return ResponseUtil.badRequest("A person cannot have a relationship with themselves");
        }

        Family family = relationshipService.getFamilyById(dto.getFamilyId());

        if (family == null) {
            return ResponseUtil.notFound("Family not found");
        }

        Person personA = relationshipService.getPersonById(dto.getPersonAId());

        if (personA == null) {
            return ResponseUtil.notFound("Person A not found");
        }

        Person personB = relationshipService.getPersonById(dto.getPersonBId());

        if (personB == null) {
            return ResponseUtil.notFound("Person B not found");
        }

        if (!relationshipService.isMember(dto.getFamilyId(), dto.getPersonAId())) {

            return ResponseUtil.conflict("Person A is not a member of this family");
        }

        if (!relationshipService.isMember(dto.getFamilyId(), dto.getPersonBId())) {

            return ResponseUtil.conflict("Person B is not a member of this family");
        }

        if (relationshipService.relationshipExists(dto)) {
            return ResponseUtil.conflict("This relationship already exists");
        }

        return ResponseUtil.created("Relationship created successfully", relationshipService.createRelationship(dto));
    }
}
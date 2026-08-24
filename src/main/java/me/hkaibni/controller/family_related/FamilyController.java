package me.hkaibni.controller.family_related;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.request.FamilyDTO;
import me.hkaibni.dto.response.FamilyTreeResponse;
import me.hkaibni.dto.search.FamilySearchDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.service.family.FamilyMemberService;
import me.hkaibni.service.family.FamilyService;
import me.hkaibni.utils.ResponseUtil;

import java.time.LocalDateTime;
import java.util.List;

@Path("/families")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FamilyController {

    @Inject
    FamilyService familyServ;
    @Inject
    FamilyMemberService familyMemberService;

    @GET
//    @RolesAllowed("ADMIN")
    public Response getAllFamilies() {
        List<Family> families = familyServ.getAllFamilies();
        if (families==null)
            return ResponseUtil.notFound("No Families Found");
        return ResponseUtil.ok("Families Retrieved Successfully",families);
    }


    @POST
    @RolesAllowed("ADMIN")
    public Response createFamily(FamilyDTO dto) {

        if (dto == null || (dto.getNameEn() == null || dto.getNameEn().isEmpty())) {
            return ResponseUtil.notFound("Family data is missing");
        }
        if (!familyServ.isUnique(dto)) {
            return ResponseUtil.conflict("A family with with these names already exists");
        }

        return ResponseUtil.created("Family created successfully",familyServ.createFamily(dto));
    }


    @GET
    @Path("/id/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response getFamilyById(@PathParam("id") String id) {
        Family family = familyServ.getFamilyById(id);
        if (family == null)
            return ResponseUtil.notFound("Family not found");
        return ResponseUtil.ok("Family retrieved successfully",family);
    }


    @GET
    @Path("/name/{familyName}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response getFamilyByName(@PathParam("familyName") String familyName) {

        Family family = familyServ.getFamilyByNameEn(familyName);
        if (family == null)
            return ResponseUtil.notFound("Family not found");
        return ResponseUtil.ok("Family Retrieved Successfully",family);

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

        return ResponseUtil.ok(results.isEmpty()
                ? "No users found"
                : "User search completed successfully",results);

    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateFamily(
            @PathParam("id") String id,
            FamilyDTO dto
    ) {
        if (!familyServ.isUnique(dto))
            return ResponseUtil.conflict("Family name already exists");
        Family updatedFamily = familyServ.updateFamily(id, dto);
        if (updatedFamily == null)
            return ResponseUtil.notFound("Family not found");

       return ResponseUtil.ok("Family updated successfully",updatedFamily);

    }

    @DELETE
    @Path("/id/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteFamilyById(@PathParam("id") String id) {

        Family family = familyServ.getFamilyById(id);
        if (family == null)
            return ResponseUtil.notFound("Family not found");

        return ResponseUtil.ok("Family deleted successfully",
                familyServ.deleteFamilyById(id));
    }


    @GET
    @Path("/{id}/tree")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFamilyTree(@PathParam("id") String id) {
        Family family = familyServ.getFamilyById(id);
        if (family == null)
            return ResponseUtil.notFound("Family not found");
        return ResponseUtil.ok("Family tree retrieved successfully",familyServ.buildTree(id));
    }

}
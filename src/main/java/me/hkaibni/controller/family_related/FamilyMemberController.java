package me.hkaibni.controller.family_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.FamilyMemberDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.family.FamilyMember;
import me.hkaibni.model.family.Person;
import me.hkaibni.repository.family.FamilyMemberRepository;
import me.hkaibni.repository.family.FamilyRepository;
import me.hkaibni.repository.family.PersonRepository;
import me.hkaibni.service.family.FamilyMemberService;
import me.hkaibni.utils.ResponseUtil;

import java.time.LocalDateTime;

@Path("/members")
public class FamilyMemberController {

    @Inject
    FamilyMemberService familyMemberServ;


    @Inject
    FamilyRepository familyRepository;

    @Inject
    PersonRepository personRepository;

    @Inject
    FamilyMemberRepository familyMemberRepository;

    @POST
    @Path("/create")
    @RolesAllowed({"USER","ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response createMember(FamilyMemberDTO dto) {

        if (dto == null || dto.getFamily() == null || dto.getPerson() == null) {
            return ResponseUtil.badRequest("Missing Member data");
        }

        Family family = familyRepository.findFamilyById(dto.getFamily());
        if (family == null) {
            return ResponseUtil.notFound("Family not found");
        }

        Person person = personRepository.findById(dto.getPerson());
        if (person == null) {
            return ResponseUtil.notFound("Person not found");
        }

        FamilyMember existingMember = familyMemberRepository.findByPersonAndFamily(person, family);

        if (existingMember != null) {
            return ResponseUtil.conflict("Members Already Exist");
        }

        return ResponseUtil.ok(
                "Member created successfully",
                familyMemberServ.createFamilyMember(dto, family, person)
        );
    }
}




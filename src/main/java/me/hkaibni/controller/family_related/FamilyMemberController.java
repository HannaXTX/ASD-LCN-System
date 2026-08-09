package me.hkaibni.controller.family_related;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.entity_dto.FamilyMemberDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.service.family.FamilyMemberService;

import java.time.LocalDateTime;

@Path("/members")
public class FamilyMemberController {

    @Inject
    FamilyMemberService familyMemberServ;


    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMember(FamilyMemberDTO dto) throws Exception {
        if (familyMemberServ.createFamilyMember(dto)==3)
            return Response.status(Response.Status.CONFLICT)
                    .entity(
                            new ApiResponse(
                                    409,
                                    "Member already exists!",
                                    null, LocalDateTime.now()
                            )
                    )
                    .build();
        if (familyMemberServ.createFamilyMember(dto)==0)
            return Response.status(Response.Status.CREATED)
                    .entity(
                            new ApiResponse(
                                    201,
                                    "Member Creation Successful",
                                    null,LocalDateTime.now()
                            )
                    )
                    .build();
        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                "ERROR",
                                null, LocalDateTime.now()
                        )
                )
                .build();
    }


}

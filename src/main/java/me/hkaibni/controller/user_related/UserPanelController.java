package me.hkaibni.controller.user_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.entity_dto.UserDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.userdata.PanelUser;
import me.hkaibni.service.users.PanelUserService;
import me.hkaibni.service.status.UpdateStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Path("/panel")


public class UserPanelController {
    @Inject
    PanelUserService userServ;


    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/test")
    public String test() {
        return jwt.getSubject() + " " + jwt.getGroups();
    }


    @GET
    @RolesAllowed("ADMIN")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getPanelUsers(){
        return Response.ok(
                new ApiResponse(
                        200,
                        "Users retrieved successfully",
                        userServ.getAllUserPanels(), LocalDateTime.now()
                )
        ).build();
    }

    @Path("/{SSN}")
    @GET
    @RolesAllowed("USER")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("SSN") String SSN){

        PanelUser user = userServ.getUserPanel(SSN);

        return Response.ok(
                new ApiResponse(
                        200,
                        "UserPanel retrieved successfully",
                        user,LocalDateTime.now()
                )
        ).build();
    }

    @Path("/{SSN}")
    @DELETE
    @Produces (MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("SSN") String SSN){

        if (!userServ.deleteUserPanel(SSN)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "UserPanel not found",
                                    null,LocalDateTime.now()
                            )
                    )
                    .build();        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "UserPanel deleted successfully",
                        null,LocalDateTime.now()
                )
        ).build();
    }
    @Path("/{id}")
    @PUT
    @RolesAllowed("USER")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateUser(UserDTO user,@PathParam("id") UUID id) throws Exception {
        UpdateStatus operation = userServ.updateUserPanel(id,user);
        if (operation==UpdateStatus.SUCCESS)
            return Response.ok(
                    new ApiResponse(
                            200,
                            "UserPanel updated successfully",
                            user,LocalDateTime.now()
                    )
            ).build();
        else if (operation==UpdateStatus.NOT_FOUND){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,LocalDateTime.now()
                    ))
                    .build();

        }
        else {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ApiResponse(
                            409,
                            "Name Already exists",
                            null,LocalDateTime.now()
                    ))
                    .build();
        }
    }

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO user) throws Exception {

        if (userServ.getUserPanel(user.getSsn()) != null)
            return Response.status(Response.Status.CONFLICT)
                    .entity(
                            new ApiResponse(
                                    409,
                                    "SSN already exists!",
                                    user.getSsn(), LocalDateTime.now()
                            )
                    )
                    .build();
        if (userServ.createUser(user))
            return Response.status(Response.Status.CREATED)
                    .entity(
                            new ApiResponse(
                                    201,
                                    "Registration Successful, UserPanel created, Please enter OTP",
                                    userServ.getUserPanel(user.getSsn()), LocalDateTime.now()
                            )
                    )
                    .build();
        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                "SSN already exists!",
                                user.getSsn(), LocalDateTime.now()
                        )
                )
                .build();
    }


    @Path("/approve")
    @POST
    @RolesAllowed("ADMIN")
    @Produces (MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approve(UserDTO dto) throws Exception {
        int operation = userServ.approve(dto.getId());
        if (operation==0)
            return Response.ok(
                    new ApiResponse(
                            200,
                            "UserPanel approved successfully",
                            dto.getId(),LocalDateTime.now(),jwt.getRawToken()
                    )
            ).build();
        else if (operation==1){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,LocalDateTime.now()
                    ))
                    .build();

        }
        return null;
    }


    @GET
    @Path("/search")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(UserSearchDTO dto) {
        List<PanelUser> results;
        if (dto.hasNoCriteria()) {
            results = userServ.searchUserPanels(dto.getValue(),dto.getPage(),dto.getPageSize());
        }
        else {
            results = userServ.searchUserPanels(dto);
        }
        return Response.ok(
                new ApiResponse(
                        200,
                        results.isEmpty()
                                ? "No users found"
                                : "UserPanel search completed successfully",
                        results,
                        LocalDateTime.now()
                )
        ).build();
    }



}

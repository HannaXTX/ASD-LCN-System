package me.hkaibni.controller;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.ApiResponse;
import me.hkaibni.dto.UserDTO;
import me.hkaibni.dto.UserSearchDTO;
import me.hkaibni.model.User;
import me.hkaibni.repository.UserRepository;
import me.hkaibni.service.UserService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path("/users")


public class UserController {
    @Inject
    UserService userServ;


    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/test")
    public String test() {
        return jwt.getSubject() + " " + jwt.getGroups();
    }


    @GET
    @RolesAllowed("USER")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getUsers(){
        return Response.ok(
                new ApiResponse(
                        200,
                        "Users retrieved successfully",
                        userServ.getAllUsers(),new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @Path("/{SSN}")
    @GET
    @RolesAllowed("USER")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("SSN") String SSN){

        User user = userServ.getUser(SSN);

        return Response.ok(
                new ApiResponse(
                        200,
                        "User retrieved successfully",
                        user,new Date(System.currentTimeMillis())
                )
        ).build();
    }

    @Path("/{SSN}")
    @DELETE
    @Produces (MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("SSN") String SSN){

        if (!userServ.deleteUser(SSN)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "User not found",
                                    null,new Date(System.currentTimeMillis())
                            )
                    )
                    .build();        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "User deleted successfully",
                        null,new Date(System.currentTimeMillis())
                )
        ).build();
    }
    @Path("/{id}")
    @PUT
    @RolesAllowed("USER")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateUser(UserDTO user,@PathParam("id") UUID id) throws Exception {
        int operation = userServ.updateUser(id,user);
        if (operation==0)
            return Response.ok(
                    new ApiResponse(
                            200,
                            "User updated successfully",
                            user,new Date(System.currentTimeMillis())
                    )
            ).build();
        else if (operation==1){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,new Date(System.currentTimeMillis())
                    ))
                    .build();

        }
        else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            409,
                            "Name Already exists",
                            null,new Date(System.currentTimeMillis())
                    ))
                    .build();
        }
    }

    @Path("/register")
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO user) throws Exception {
        if (userServ.getUser(user.getSSN())!=null)
            return Response.status(Response.Status.CONFLICT)
                    .entity(
                            new ApiResponse(
                                    409,
                                    "SSN already exists!",
                                    user.getSSN(),new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        if (userServ.createUser(user))
            return Response.status(Response.Status.CREATED)
                    .entity(
                            new ApiResponse(
                                    201,
                                    "Registration Successful, User created, Please enter OTP",
                                    userServ.getUser(user.getSSN()),new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                "SSN already exists!",
                                user.getSSN(),new Date(System.currentTimeMillis())
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
                            "User approved successfully",
                            dto.getId(),new Date(System.currentTimeMillis()),jwt.getRawToken()
                    )
            ).build();
        else if (operation==1){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,new Date(System.currentTimeMillis())
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
        List<User> results;
        if (dto.hasNoCriteria()) {
            results = userServ.searchUsers(dto.getValue());
        }
        else {
            results = userServ.searchUsers(dto);
        }
        return Response.ok(
                new ApiResponse(
                        200,
                        results.isEmpty()
                                ? "No users found"
                                : "User search completed successfully",
                        results,
                        new Date()
                )
        ).build();
    }



}

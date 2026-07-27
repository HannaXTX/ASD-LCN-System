package me.hkaibni.controller;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.ApiResponse;
import me.hkaibni.dto.LoginDTO;
import me.hkaibni.dto.UserDTO;
import me.hkaibni.model.User;
import me.hkaibni.service.AuthService;
import me.hkaibni.service.UserService;

import java.util.Date;

@Path("/login")
public class AuthController {

    public enum STATE {SUCCESS,INVALID_CRED, PENDING_APR,PENDING_VER}


    @Inject
    UserService userServ;
    @Inject
    AuthService authServ;




    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) throws Exception {

       STATE result = authServ.login(loginDTO);

        if (result == STATE.INVALID_CRED) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(
                            new ApiResponse(
                                    401,
                                    "Invalid SSN or password",
                                    null,new Date(System.currentTimeMillis())

                            )
                    )
                    .build();
        }

        if (result == STATE.PENDING_APR) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(
                            new ApiResponse(
                                    403,
                                    "Unverified Account, Please follow OTP verification procedure",
                                    null,new Date(System.currentTimeMillis())

                            )
                    )
                    .build();
        }

        if (result == STATE.PENDING_VER) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(
                            new ApiResponse(
                                    403,
                                    "Unapproved Account, Please wait for Account Approval",
                                    null,new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        User user = userServ.getUser(loginDTO.getSSN());

        return Response.ok(
                new ApiResponse(
                        200,
                        "Login successful",
                        user,
                        new Date(System.currentTimeMillis()),
                        user.getAccount().getToken()
                )
        ).build();
    }


}

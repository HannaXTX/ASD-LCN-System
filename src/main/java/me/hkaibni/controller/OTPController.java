package me.hkaibni.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.ApiResponse;
import me.hkaibni.dto.OTPDTO;
import me.hkaibni.model.OTP;
import me.hkaibni.model.User;
import me.hkaibni.security.GFG;
import me.hkaibni.service.AuthService;
import me.hkaibni.service.OTPService;
import me.hkaibni.service.UserService;

import java.util.Date;

@Path("/otp")
public class OTPController {

    public enum STATE {SUCCESS,INVALID_CRED,PENDING_OTP,PENDING_VER}

    public enum otpState {SUCCESS,NULL,OUT_OF_ATTEMPTS}


    @Inject
    UserService userServ;
    @Inject
    AuthService authServ;
    @Inject
    OTPService otpServ;
    
    @Path("/{SSN}")
    @GET
    public Response obtainOTP(@PathParam("SSN") String SSN) throws Exception {

        User user = userServ.getUser(SSN);

        otpState result = otpServ.createOTP(
                user,
                "REGISTRATION",
                GFG.getOTP_CODE()
        );

        if (result == otpState.NULL) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "User not found",
                                    null,
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        if (result == otpState.OUT_OF_ATTEMPTS) {
            return Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity(
                            new ApiResponse(
                                    429,
                                    "OTP attempts exceeded",
                                    null,
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        if (result == otpState.SUCCESS) {
            OTP otp = otpServ.getOTP(user);

            return Response.status(Response.Status.OK)
                    .entity(
                            new ApiResponse(
                                    200,
                                    "OTP returned successfully",
                                    otp.getHashedOtp(),
                                    new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(
                        new ApiResponse(
                                500,
                                "Unknown OTP state",
                                null,
                                new Date(System.currentTimeMillis())
                        )
                )
                .build();
    }

    @Path("/{SSN}")
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response verify(OTPDTO dto, @PathParam("SSN") String SSN) throws Exception {
        User user = userServ.getUser(SSN);

        OTP otp = otpServ.getOTP(user);
        if (otpServ.checkOTP(otp.getHashedOtp(),dto.getOtpcode())){
            otpServ.verifyUser(user,otp);
            return Response.status(Response.Status.OK)
                    .entity(
                            new ApiResponse(
                                    200,
                                    "Account Verified",
                                    otp.getPurpose(),new Date(System.currentTimeMillis())
                            )
                    )
                    .build();
        }


        return Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(
                        new ApiResponse(
                                406,
                                "WRONG OTP",
                                otp.getHashedOtp(),new Date(System.currentTimeMillis())
                        )
                )
                .build();
    }


}


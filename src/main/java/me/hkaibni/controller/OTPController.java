package me.hkaibni.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.entity_dto.OTPDTO;
import me.hkaibni.dto.entity_dto.UserDTO;
import me.hkaibni.model.OTP;
import me.hkaibni.model.userdata.User;
import me.hkaibni.security.GFG;
import me.hkaibni.service.AuthService;
import me.hkaibni.service.OTPService;
import me.hkaibni.service.UserService;
import me.hkaibni.service.results.OtpStatus;

import java.time.LocalDateTime;

@Path("/otp")
public class OTPController {

    @Inject
    UserService userServ;
    @Inject
    AuthService authServ;
    @Inject
    OTPService otpServ;
    
    @Path("/request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtainOTP(UserDTO dto) throws Exception {

//        User user = userServ.getUser(dto.getSSN());
        User user = userServ.getUser(dto.getId());
        OtpStatus result = otpServ.createOTP(
                user,
                "REGISTRATION",
                GFG.getOTP_CODE()
        );

        if (result == OtpStatus.NULL) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "User not found",
                                    user,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        if (result == OtpStatus.OUT_OF_ATTEMPTS) {
            return Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity(
                            new ApiResponse(
                                    429,
                                    "OTP attempts exceeded",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        if (result == OtpStatus.SUCCESS) {
            OTP otp = otpServ.getOTP(user);

            return Response.status(Response.Status.OK)
                    .entity(
                            new ApiResponse(
                                    200,
                                    "OTP returned successfully",
                                    otp.getHashedOtp(),
                                    LocalDateTime.now()
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
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    @Path("/verify")
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response verify(OTPDTO dto) throws Exception {
        User user = userServ.getUser(dto.getSsn());

        OTP otp = otpServ.getOTP(user);
        if (otpServ.checkOTP(otp.getHashedOtp(),dto.getOtpcode())){
            otpServ.verifyUser(user,otp);
            return Response.status(Response.Status.OK)
                    .entity(
                            new ApiResponse(
                                    200,
                                    "Account Verified",
                                    otp.getPurpose(),LocalDateTime.now()
                            )
                    )
                    .build();
        }


        return Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(
                        new ApiResponse(
                                406,
                                "WRONG OTP",
                                otp.getHashedOtp(),LocalDateTime.now()
                        )
                )
                .build();
    }


}


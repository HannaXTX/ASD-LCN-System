package me.hkaibni.controller.user_related;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.*;
import me.hkaibni.dto.otp.OTPRequestDTO;
import me.hkaibni.dto.otp.OTPVerifyDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.response.IdResponse;
import me.hkaibni.model.roles_types.OtpPurpose;
import me.hkaibni.model.userdata.User;
import me.hkaibni.model.userdata.PanelUser;
import me.hkaibni.security.GFG;
import me.hkaibni.service.status.OtpStatus;
import me.hkaibni.service.status.resetPasswordStatus;
import me.hkaibni.service.users.AuthService;
import me.hkaibni.service.users.OTPService;
import me.hkaibni.service.users.PanelUserService;
import me.hkaibni.service.users.UserService;
import me.hkaibni.service.status.LoginStatus;
import me.hkaibni.utils.ResponseUtil;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;

@Path("/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints used to authenticate panel administrators and community users."
)
public class AuthController {

    @Inject
    UserService userServ;
    @Inject
    PanelUserService userPanelService;
    @Inject
    AuthService authServ;
    @Inject
    JsonWebToken jwt;
    @Inject
    OTPService otpServ;


    @Operation(
            operationId = "loginCommunityUser",
            summary = "Login community user",
            description = """
                Authenticates an approved and OTP-verified community user.
                Returns user details and a JWT access token on success.
                """
    )

    @RequestBody(
            required = true,
            description = "Community user login credentials.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = LoginDTO.class),
                    examples = @ExampleObject(
                            name = "Valid credentials",
                            value = """
                                {
                                  "ssn": "123456789",
                                  "password": "SecurePassword123!"
                                }
                                """
                    )
            )
    )


    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Missing or invalid request data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Invalid SSN or password",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Account is not verified or not approved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),

    })

    @Path("/users/login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(LoginDTO loginDTO) throws Exception {


       if (loginDTO.getSsn() == null || loginDTO.getSsn().isBlank())
           return ResponseUtil.badRequest("Empty SSN field");

        if (loginDTO.getPassword().isBlank())
            return ResponseUtil.badRequest("Empty Password field");

        LoginStatus result = authServ.loginUser(loginDTO);

        if (result == LoginStatus.INVALID_CRED) {
            return ResponseUtil.unauthorized("Invalid SSN or Password");
        }

        if (result == LoginStatus.PENDING_VER) {
            return ResponseUtil.forbidden("Unverified Account, Please follow OTP verification procedure");
        }

        if (result == LoginStatus.PENDING_APR) {
            return ResponseUtil.forbidden("Unapproved Account, Please wait for Account Approval");
        }

        User user = userServ.getUserBySsn(loginDTO.getSsn());
        UserSummaryDTO userSummaryDTO = new UserSummaryDTO();
        userSummaryDTO.setFirstName(user.getPerson().getFirstNameEn());
        userSummaryDTO.setLastName(user.getPerson().getLastNameEn());
        userSummaryDTO.setId(user.getId());
        userSummaryDTO.setRole(user.getAccount().getUserType().getPrivilege());

        return Response.ok(
                new ApiResponse(
                        200,
                        "Login successful",
                        userSummaryDTO,
                        LocalDateTime.now(),
                        user.getAccount().getToken()
                )
        ).build();
    }

    @Operation(
            operationId = "loginPanelUser",
            summary = "Login Panel User (Admin)",
            description = """
               
                """
    )

    @Path("/panel/login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPanel(PanelLoginDTO loginDTO) throws Exception {

        if (loginDTO.getUsername() == null || loginDTO.getUsername().isBlank())
            return ResponseUtil.badRequest("Empty username field");

        if (loginDTO.getPassword().isBlank())
            return ResponseUtil.badRequest("Empty Password field");

        LoginStatus result = authServ.loginPanel(loginDTO);

        if (result == LoginStatus.INVALID_CRED) {
            return ResponseUtil.unauthorized("Invalid username or Password");
        }

        PanelUser user = userPanelService.getUserPanelByUsername(loginDTO.getUsername());

        return Response.ok(
                new ApiResponse(
                        200,
                        "Login successful",
                        user,
                        LocalDateTime.now(),
                        user.getAccount().getToken()
                )
        ).build();
    }
    @Operation(
            operationId = "reset-password",
            summary = "Sends a Reset password request that updates the password on success",
            description = """
                Available After the User identifies himself using
                ssn on /reset-password/identify and provides Otp on /reset-password/request
                """
    )

    @Path("/reset-password")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(PasswordResetDTO dto) throws Exception {
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "NULL REQUEST",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }
        resetPasswordStatus result = authServ.updatePassword(dto);

        if (result == resetPasswordStatus.NULL_USER) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        if (result == resetPasswordStatus.NULL_PASSWORD) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            400,
                            "Null Password",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        if (result == resetPasswordStatus.SAME_PASSWORD) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            400,
                            "New password cannot be the same as the old password",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        if (result == resetPasswordStatus.SUCCESS) {
            return Response.ok(
                    new ApiResponse(
                            200,
                            "Password reset successfully",
                            null,
                            LocalDateTime.now()
                    )
            ).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(
                        500,
                        "Unable to reset password",
                        null,
                        LocalDateTime.now()
                ))
                .build();
    }

    @Path("/change-password/request")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPasswordRequest(OTPRequestDTO dto) throws Exception {

        if (dto == null || dto.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            400,
                            "Invalid password reset request",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        User user = userServ.getUserById(dto.getId());

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        OtpStatus result = otpServ.createOTP(
                user,
                OtpPurpose.PASSWORD_RESET,
                GFG.getOTP_CODE()
        );

        if (result == OtpStatus.OUT_OF_ATTEMPTS) {
            return Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity(new ApiResponse(
                            429,
                            "OTP attempts exceeded",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        if (result == OtpStatus.SUCCESS) {
            return Response.ok(
                    new ApiResponse(
                            200,
                            "Password reset OTP sent successfully",
                            null,
                            LocalDateTime.now()
                    )
            ).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse(
                        500,
                        "Unable to create password reset request",
                        null,
                        LocalDateTime.now()
                ))
                .build();
    }

    @Path("/reset-password/identify")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response identifyResetPasswordUser(ResetPasswordIdentifyDTO dto) throws Exception {

        if (dto == null || dto.getSsn() == null || dto.getSsn().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            400,
                            "SSN is required",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }

        User user = userServ.getUserBySsn(dto.getSsn());


        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "User not found",
                            null,
                            LocalDateTime.now()
                    ))
                    .build();
        }
        otpServ.createOTP(
                user, OtpPurpose.PASSWORD_RESET, GFG.getOTP_CODE()
        );
        IdResponse idResponse = new IdResponse(user.getId());
        return Response.ok(
                new ApiResponse(
                        200,
                        "User identified, Sending OTP",
                        idResponse,
                        LocalDateTime.now()
                )
        ).build();
    }


    @Path("/reset-password/verify")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPasswordOtpVerify(OTPVerifyDTO dto) {

        OtpStatus result = otpServ.verifyOtpReset(
                dto.getId(),
                dto.getOtpCode()
        );
        IdResponse idResponse = new IdResponse(dto.getId());
        if (result == OtpStatus.SUCCESS) {
            return Response.ok(
                    new ApiResponse(
                            200,
                            "User Verified",
                            idResponse,
                            LocalDateTime.now()
                    )
            ).build();
        }

        if (result == OtpStatus.NOT_FOUND) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "User or OTP not found",
                                    null,
                                    LocalDateTime.now()
                            )
                    )
                    .build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(
                        new ApiResponse(
                                406,
                                "Wrong OTP",
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }
}

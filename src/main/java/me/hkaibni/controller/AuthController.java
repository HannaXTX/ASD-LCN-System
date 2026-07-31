package me.hkaibni.controller;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.entity_dto.LoginDTO;
import me.hkaibni.model.User;
import me.hkaibni.model.UserPanel;
import me.hkaibni.service.AuthService;
import me.hkaibni.service.UserPanelService;
import me.hkaibni.service.UserService;
import me.hkaibni.service.results.LoginStatus;
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
    UserPanelService userPanelService;
    @Inject
    AuthService authServ;

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

       LoginStatus result = authServ.loginUser(loginDTO);

        if (result == LoginStatus.INVALID_CRED) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(
                            new ApiResponse(
                                    401,
                                    "Invalid SSN or password",
                                    null,LocalDateTime.now()

                            )
                    )
                    .build();
        }

        if (result == LoginStatus.PENDING_APR) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(
                            new ApiResponse(
                                    403,
                                    "Unverified Account, Please follow OTP verification procedure",
                                    null,LocalDateTime.now()

                            )
                    )
                    .build();
        }

        if (result == LoginStatus.PENDING_VER) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(
                            new ApiResponse(
                                    403,
                                    "Unapproved Account, Please wait for Account Approval",
                                    null,LocalDateTime.now()
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
                        LocalDateTime.now(),
                        user.getAccount().getToken()
                )
        ).build();
    }


    @Path("/panel/login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPanel(LoginDTO loginDTO) throws Exception {

        LoginStatus result = authServ.loginPanel(loginDTO);

        if (result == LoginStatus.INVALID_CRED) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(
                            new ApiResponse(
                                    401,
                                    "Invalid SSN or password",
                                    null,LocalDateTime.now()

                            )
                    )
                    .build();
        }

        if (result == LoginStatus.PENDING_VER) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(
                            new ApiResponse(
                                    403,
                                    "Unverified Account, Please follow OTP verification procedure",
                                    null,LocalDateTime.now()

                            )
                    )
                    .build();
        }

        if (result == LoginStatus.PENDING_APR) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(
                            new ApiResponse(
                                    403,
                                    "Unapproved Account, Please wait for Account Approval",
                                    null,LocalDateTime.now()
                            )
                    )
                    .build();
        }

        UserPanel user = userPanelService.getUserPanel(loginDTO.getSSN());

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



}

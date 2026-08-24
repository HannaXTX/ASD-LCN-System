package me.hkaibni.controller.user_related;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.IdRequest;
import me.hkaibni.dto.request.UserUpdateDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.request.UserDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.model.userdata.User;
import me.hkaibni.repository.media.AttachmentRepository;
import me.hkaibni.service.media.AttachmentService;
import me.hkaibni.service.users.UserService;
import me.hkaibni.service.status.UpdateStatus;
import me.hkaibni.utils.ResponseUtil;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Path("/users")
@Tag(
        name = "User APIs",
        description = "Endpoints for user and administrator operations on normal user data."
)

public class UserController {
    @Inject
    UserService userServ;
    @Inject
    JWTParser parser;


    @Inject
    JsonWebToken jwt;
    @Inject
    AttachmentRepository attachmentRepository;
    @Inject
    AttachmentService attachmentService;

    @GET
    @Path("/test")
    public String test() {
        return jwt.getSubject() + " " + jwt.getGroups();
    }


    @Operation(
            operationId = "getAllUsers",
            summary = "Returns all Users in DB as List",
            description = """
                Returns all Users as List when requested by Panel Users.
                """
    )

    @GET
    @RolesAllowed("ADMIN")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getUsers(){
        return Response.ok(
                new ApiResponse(
                        200,
                        "Users retrieved successfully",
                        userServ.getAllUsers(), LocalDateTime.now()
                )
        ).build();
    }

    @Operation(
            operationId = "getUserBySSN",
            summary = "get a User by SSN",
            description = """
                returns User by SSN.
                """
    )

    @Path("/{SSN}")
    @GET
    @RolesAllowed({"USER","ADMIN"})
    @Produces (MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("SSN") String SSN){

        User user = userServ.getUserBySsn(SSN);

        return Response.ok(
                new ApiResponse(
                        200,
                        "User retrieved successfully",
                        user,LocalDateTime.now()
                )
        ).build();
    }
    @Operation(
            operationId = "deleteUserBySSN",
            summary = "Deletes User by SSN",
            description = """
                Deletes User by SSN.
                """
    )

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
                                    null,LocalDateTime.now()
                            )
                    )
                    .build();        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "User deleted successfully",
                        null,LocalDateTime.now()
                )
        ).build();
    }
    @Path("/update")
    @PUT
    @RolesAllowed({"USER","ADMIN"})
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateUser(UserUpdateDTO user) throws Exception {
        UpdateStatus operation = userServ.updateUser(jwt.getSubject(),user);
        if (operation==UpdateStatus.SUCCESS)
            return Response.ok(
                    new ApiResponse(
                            200,
                            "User updated successfully",
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
    @Operation(
            operationId = "registerUser",
            summary = "User inputs data to register",
            description = """
                User inputs data that will create 3 Entities:
                1. Person (Contains personal information for Tree generation),
                2. User  (basic user data that is not required in Tree),
                3. Account (Sensitive info and tracking for user verification and approval)
                
                a User must go through a series of steps to Login:
                1. User Registers on /users/register, otp/request is called to generate an OTP for User
                2. User enters OTP to verify account on /otp/verify
                3. User waits for admin approval on /users/approve
                4. User can Login
                """
    )
    @Path("/register")
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO dto) throws Exception {

        if (dto.getSsn() == null || dto.getSsn().isBlank())
            return ResponseUtil.badRequest("Empty Data fields");

        if (userServ.getUserBySsn(dto.getSsn())!=null)
            return ResponseUtil.conflict("SSN already exists!");

        User user = userServ.createUser(dto);

        if (user!=null)
            return ResponseUtil.created("Registration Successful, User created, Please enter OTP",user);

        return ResponseUtil.conflict("SSN already exists!");

    }

    @Operation(
            operationId = "approveUser",
            summary = "panel user (Admin) approves User",
            description = """
                Admin approves User which modifies approved from 0 to 1 on the connected Account entity
                this allows the user to Login if verified using OTP on otp/request + otp/verify
                """
    )
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
        List<User> results;
        if (dto.hasNoCriteria()) {
            results = userServ.searchUsers(dto.getValue(),dto.getPage(),dto.getPageSize());
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
                        LocalDateTime.now()
                )
        ).build();
    }

    @Path("/me")
    @GET
    @RolesAllowed({"USER","ADMIN"})
    @Produces (MediaType.APPLICATION_JSON)
    public Response getMyData() throws ParseException {
//        JsonWebToken jwt = parser.parse(tokenDTO.getToken());
        String userId = jwt.getSubject();
        User user = userServ.getUserById(userId);

        return Response.ok(
                new ApiResponse(
                        200,
                        "User retrieved successfully",
                        user,LocalDateTime.now()
                )
        ).build();
    }

    @Path("/me/profile-picture")
    @POST
    @RolesAllowed({"USER","ADMIN"})
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateMyProfilePicture(IdRequest idReq){

        if(idReq ==null || idReq.getId().isEmpty())
            return ResponseUtil.badRequest("No Attachment Provided");

        User user = userServ.getUserById(jwt.getSubject());
        if (user == null)
            return ResponseUtil.notFound("User not found");
        Attachment attachment = attachmentService.findById(idReq.getId());

        if (attachment == null)
            return ResponseUtil.notFound("Attachment not found");

        Person updatedUserPerson = userServ.updateProfilePicture(user,attachment.getId());
        return ResponseUtil.ok("Profile Picture Updated!", updatedUserPerson);
    }


    @Path("/me/picture")
    @GET
    @RolesAllowed({"USER","ADMIN"})
    @Produces (MediaType.APPLICATION_JSON)
    public Response getMyPicture(){

        User user = userServ.getUserById(jwt.getSubject());
        if (user == null)
            return ResponseUtil.notFound("User not found");
        Attachment attachment = attachmentService.findById(user.getPerson().getProfilePicture());
        if (attachment == null)
            return ResponseUtil.notFound("Attachment not found");

        return ResponseUtil.ok("Returned Profile Picture!", attachment);
    }
}

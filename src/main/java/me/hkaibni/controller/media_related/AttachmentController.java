package me.hkaibni.controller.media_related;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.AttachmentUploadDTO;
import me.hkaibni.dto.search.AttachmentSearchDTO;
import me.hkaibni.dto.search.PersonSearchDTO;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.repository.media.AttachmentRepository;
import me.hkaibni.service.media.AttachmentService;
import me.hkaibni.utils.ResponseUtil;

import java.util.List;

@Path("/attachments")
@Produces(MediaType.APPLICATION_JSON)
public class AttachmentController {

    @Inject
    AttachmentService attachmentService;
    @Inject
    AttachmentRepository attachmentRepository;

    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadFile(
            AttachmentUploadDTO dto,

            @HeaderParam("X-File-Name")
            String originalName,

            @HeaderParam(HttpHeaders.CONTENT_TYPE)
            String contentType

    ) {

//        if (dto.getFileData() == null || dto.getFileData().length == 0) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Map.of(
//                            "message", "File data is required"
//                    ))
//                    .build();
//        }
//
//        if (originalName == null || originalName.isBlank()) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Map.of(
//                            "message", "X-File-Name header is required"
//                    ))
//                    .build();
//        }



        return Response.status(Response.Status.CREATED)
                .entity(attachmentService.upload(dto))
                .build();
    }

    @POST
    @Path("/multi-upload")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadMultipleFile(
            List<AttachmentUploadDTO> dto
    ) {
        return Response.status(Response.Status.CREATED)
                .entity(attachmentService.uploadMulti(dto))
                .build();
    }

    @GET
    @Path("/{id}/view")
    public Response viewAttachment(@PathParam("id") String id) {

        Attachment attachment = attachmentRepository.findById(id);

        if (attachment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(attachment.getFileData())
                .type(attachment.getContentType())
                .header(
                        "Content-Disposition",
                        "inline; filename=\"" + attachment.getOriginalName() + "\""
                )
                .header("Content-Length", attachment.getFileData().length)
                .build();
    }

    @GET
    @Path("/search")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchAttachments(AttachmentSearchDTO dto) {
        List<Attachment> results;
        if (dto.hasNoCriteria()) {
            results = attachmentService.searchAttachments(dto.getValue(),dto.getPage(),dto.getPageSize());
        }
        else {
            results = attachmentService.searchAttachments(dto);
        }

        return ResponseUtil.ok(results.isEmpty()
                ? "No Attachments found"
                : "Attachment search completed successfully",results);

    }
}
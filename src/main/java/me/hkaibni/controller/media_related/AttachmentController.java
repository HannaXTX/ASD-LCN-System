package me.hkaibni.controller.media_related;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.entity_dto.AttachmentUploadDTO;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.repository.media.AttachmentRepository;
import me.hkaibni.service.media.AttachmentService;

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
}
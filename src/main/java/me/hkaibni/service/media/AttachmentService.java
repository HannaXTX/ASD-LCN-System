package me.hkaibni.service.media;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import me.hkaibni.dto.request.AttachmentUploadDTO;
import me.hkaibni.dto.search.AttachmentSearchDTO;
import me.hkaibni.dto.search.PersonSearchDTO;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.repository.media.AttachmentRepository;
import me.hkaibni.repository.user.UserRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AttachmentService {


    @Inject
    JsonWebToken jwt;
    @Inject
    UserRepository userRepository;
    @Inject
    AttachmentRepository attachmentRepository;

    public List<Attachment> searchAttachments(AttachmentSearchDTO request) {
        return attachmentRepository.search(request);
    }

    public List<Attachment> searchAttachments(String request, int page, int pageSize) {
        return attachmentRepository.search(request,page,pageSize);
    }

    public Attachment findById(String id){
        return attachmentRepository.findById(id);
    }

    @Transactional
    public Attachment upload(AttachmentUploadDTO dto){



    //    User user = userRepository.findById(jwt.getSubject());


        if (jwt.getSubject() == null) {
            throw new NotFoundException("User not found");
        }

//
//        if (contentType == null || contentType.isBlank()) {
//            contentType = MediaType.APPLICATION_OCTET_STREAM;
//        }

        org.apache.tika.Tika tika = new org.apache.tika.Tika();
        String mimeType = tika.detect(dto.getFileData());


        Attachment attachment = new Attachment();
        LocalDateTime now = LocalDateTime.now();

        attachment.setId(UUID.randomUUID().toString());

        attachment.setContentType(mimeType);
        attachment.setCreatedAt(now);
        attachment.setFileData(dto.getFileData());
        attachment.setOriginalName(dto.getOriginalName());
        attachment.setUploadedBy(jwt.getSubject());
        attachment.setSizeBytes(dto.getFileData().length);

        attachmentRepository.save(attachment);


        return attachment;
    }


    @Transactional
    public List<Attachment> uploadMulti(List<AttachmentUploadDTO> listOfDto){

        List<Attachment> attachments = new ArrayList<>();

        //    User user = userRepository.findById(jwt.getSubject());


        if (jwt.getSubject() == null) {
            throw new NotFoundException("User not found");
        }

        for (AttachmentUploadDTO dto : listOfDto){
            org.apache.tika.Tika tika = new org.apache.tika.Tika();
            String mimeType = tika.detect(dto.getFileData());


            Attachment attachment = new Attachment();
            LocalDateTime now = LocalDateTime.now();

            attachment.setId(UUID.randomUUID().toString());

            attachment.setContentType(mimeType);
            attachment.setCreatedAt(now);
            attachment.setFileData(dto.getFileData());
            attachment.setOriginalName(dto.getOriginalName());
            attachment.setUploadedBy(jwt.getSubject());
            attachment.setSizeBytes(dto.getFileData().length);

            attachmentRepository.save(attachment);
            attachments.add(attachment);
        }


        return attachments;
    }

    @Transactional
    public Attachment download(AttachmentUploadDTO dto){



        //    User user = userRepository.findById(jwt.getSubject());


        if (jwt.getSubject() == null) {
            throw new NotFoundException("User not found");
        }

        String contentType = dto.getContentType();

        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }

        Attachment attachment = new Attachment();
        LocalDateTime now = LocalDateTime.now();

        attachment.setId(UUID.randomUUID().toString());

        attachment.setContentType(dto.getContentType());
        attachment.setCreatedAt(now);
        attachment.setFileData(dto.getFileData());
        attachment.setOriginalName(dto.getOriginalName());
        attachment.setUploadedBy(jwt.getSubject());
        attachment.setSizeBytes(dto.getFileData().length);

        attachmentRepository.save(attachment);


        return attachment;
    }


}

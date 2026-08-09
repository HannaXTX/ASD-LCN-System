package me.hkaibni.dto.entity_dto;

import jakarta.persistence.*;
import me.hkaibni.model.media.Attachment;

import java.time.LocalDateTime;
import java.util.List;

public class BlogDTO {


    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private String createdBy;

    @OneToMany
    private List<String> attachmentIds;

    private long raiseCount;



    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public List<String> getAttachmentIds() {
        return attachmentIds;
    }
}

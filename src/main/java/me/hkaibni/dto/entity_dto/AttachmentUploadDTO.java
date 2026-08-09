package me.hkaibni.dto.entity_dto;

public class AttachmentUploadDTO {

    private String originalName;
    private String contentType;
    private byte[] fileData;

    public AttachmentUploadDTO() {
    }

    public AttachmentUploadDTO(
            String originalName,
            String contentType,
            byte[] fileData
    ) {
        this.originalName = originalName;
        this.contentType = contentType;
        this.fileData = fileData;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
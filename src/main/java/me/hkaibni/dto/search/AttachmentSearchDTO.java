package me.hkaibni.dto.search;


import java.time.LocalDateTime;

public class AttachmentSearchDTO extends SearchDTO {

    private String originalName;
    private String contentType;
    private String uploadedBy;

    private Long minSizeBytes;
    private Long maxSizeBytes;

    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    public boolean hasNoCriteria() {
        return isBlank(originalName)
                && isBlank(contentType)
                && isBlank(uploadedBy)
                && minSizeBytes == null
                && maxSizeBytes == null
                && createdAfter == null
                && createdBefore == null;
    }
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
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

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Long getMinSizeBytes() {
        return minSizeBytes;
    }

    public void setMinSizeBytes(Long minSizeBytes) {
        this.minSizeBytes = minSizeBytes;
    }

    public Long getMaxSizeBytes() {
        return maxSizeBytes;
    }

    public void setMaxSizeBytes(Long maxSizeBytes) {
        this.maxSizeBytes = maxSizeBytes;
    }

    public LocalDateTime getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(LocalDateTime createdAfter) {
        this.createdAfter = createdAfter;
    }

    public LocalDateTime getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(LocalDateTime createdBefore) {
        this.createdBefore = createdBefore;
    }
}
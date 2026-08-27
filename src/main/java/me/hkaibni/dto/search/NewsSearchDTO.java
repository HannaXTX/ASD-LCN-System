package me.hkaibni.dto.search;

import java.time.LocalDateTime;

public class NewsSearchDTO extends SearchDTO {

    private String title;
    private String content;
    private String createdBy;

    private LocalDateTime publishedAfter;
    private LocalDateTime publishedBefore;

    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getPublishedAfter() {
        return publishedAfter;
    }

    public void setPublishedAfter(LocalDateTime publishedAfter) {
        this.publishedAfter = publishedAfter;
    }

    public LocalDateTime getPublishedBefore() {
        return publishedBefore;
    }

    public void setPublishedBefore(LocalDateTime publishedBefore) {
        this.publishedBefore = publishedBefore;
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

    public boolean hasNoCriteria() {
        return isBlank(title)
                && isBlank(content)
                && isBlank(createdBy)
                && publishedAfter == null
                && publishedBefore == null
                && createdAfter == null
                && createdBefore == null;
    }
}
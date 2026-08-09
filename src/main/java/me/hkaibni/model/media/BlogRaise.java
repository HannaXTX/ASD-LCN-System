package me.hkaibni.model.media;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "blog_raises",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"blog_id", "raised_by"}
        )
)
public class BlogRaise {

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @Column(name = "raised_by", nullable = false)
    private String raisedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
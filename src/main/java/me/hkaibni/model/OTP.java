package me.hkaibni.model;

import jakarta.persistence.*;
import me.hkaibni.model.userdata.User;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class OTP {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String Otp;

    private String purpose;

    private Integer attempts;

    private Boolean verified;



    // AUDIT DATA
    private Date createdAt;
    private Date expiresAt;
    private LocalDateTime modifiedAt;
    private String createdBy;

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String hashedOtp) {
        this.Otp = hashedOtp;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
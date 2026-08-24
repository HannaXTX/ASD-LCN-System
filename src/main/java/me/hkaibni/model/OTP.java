package me.hkaibni.model;

import jakarta.persistence.*;
import me.hkaibni.model.roles_types.OtpPurpose;
import me.hkaibni.model.userdata.User;

import java.time.LocalDateTime;

@Entity
public class OTP {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String Otp;

    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose;

    private Integer attempts;

    private Boolean verified;



    // AUDIT DATA
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
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

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
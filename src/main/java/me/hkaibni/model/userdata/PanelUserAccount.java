package me.hkaibni.model.userdata;

import jakarta.persistence.*;
import me.hkaibni.model.roles.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table (name = "panel_user_accounts")
public class PanelUserAccount {
    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String token;

    // AUDIT DATA
    private LocalDateTime lastLoggedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String createdBy;


    private byte[] salt;
    private boolean isActive;

    private int approved;
    private int verified;




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public UserRole getUserType() {
        return userRole;
    }

    public void setUserType(UserRole userType) {
        this.userRole = userType;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public LocalDateTime getLastLoggedAt() {
        return lastLoggedAt;
    }

    public void setLastLoggedAt(LocalDateTime lastLoggedAt) {
        this.lastLoggedAt = lastLoggedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

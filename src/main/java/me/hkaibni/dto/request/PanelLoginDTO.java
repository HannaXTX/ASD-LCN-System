package me.hkaibni.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class PanelLoginDTO {
    @Schema(
            description = "Panel username",
            example = "username",
            required = true
    )
    private String username;
    @Schema(
            description = "User's password",
            example = "SecurePassword123!",
            format = "password",
            required = true
    )
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

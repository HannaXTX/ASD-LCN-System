package me.hkaibni.dto.entity_dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class LoginDTO {
    @Schema(
            description = "User's Social Security Number",
            example = "123456789",
            required = true
    )
    private String SSN;
    @Schema(
            description = "User's password",
            example = "SecurePassword123!",
            format = "password",
            required = true
    )
    private String password;

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

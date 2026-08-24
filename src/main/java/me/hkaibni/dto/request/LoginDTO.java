package me.hkaibni.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class LoginDTO {
    @Schema(
            description = "User's Social Security Number",
            example = "123456789",
            required = true
    )
    private String ssn;
    @Schema(
            description = "User's password",
            example = "SecurePassword123!",
            format = "password",
            required = true
    )
    private String password;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

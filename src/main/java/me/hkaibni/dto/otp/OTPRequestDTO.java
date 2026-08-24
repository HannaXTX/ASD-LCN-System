package me.hkaibni.dto.otp;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class OTPRequestDTO {
    @Schema(
            description = "User's UUID",
            example = "cc0480a0-4e0c-4e0d-8041-401607200daf",
            required = true
    )
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

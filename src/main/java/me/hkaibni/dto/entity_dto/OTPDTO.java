package me.hkaibni.dto.entity_dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class OTPDTO {
    @Schema(
            description = "User's Social Security Number",
            example = "123456789",
            required = true
    )
    private String ssn;
    @Schema(
            description = "User's latest OTP sent",
            example = "123456",
            format = "password",
            required = true
    )
    private String otpcode;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getOtpcode() {
        return otpcode;
    }

    public void setOtpcode(String otpcode) {
        this.otpcode = otpcode;
    }

}

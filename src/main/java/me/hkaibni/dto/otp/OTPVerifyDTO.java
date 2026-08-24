package me.hkaibni.dto.otp;

public class OTPVerifyDTO {


    private String id;
    private String otpCode;

    public String getId() {
        return id;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}

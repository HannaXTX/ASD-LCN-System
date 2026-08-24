package me.hkaibni.service.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import me.hkaibni.model.OTP;
import me.hkaibni.model.roles_types.OtpPurpose;
import me.hkaibni.model.userdata.User;
import me.hkaibni.model.userdata.UserAccount;
import me.hkaibni.repository.user.OTPRepository;
import me.hkaibni.repository.user.UserRepository;
import me.hkaibni.utils.TimeSec;
import me.hkaibni.service.status.OtpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OTPService {

    @Inject
    UserRepository userRepository;
    @Inject
    OTPRepository OTPRepository;
    @Inject
    UserService userService;


    @Transactional
    public OtpStatus createOTP(User user, OtpPurpose otpPurpose, String OTP_CODE) throws Exception {

        int attempts = (int) OTPRepository.countOtpAfter(user, TimeSec.getDailyResetTime(),otpPurpose)+1;
        LocalDateTime now = LocalDateTime.now();

        if (user==null)
            return OtpStatus.NULL;
        if (attempts > 6)
            return OtpStatus.OUT_OF_ATTEMPTS;

        OTP otp = new OTP();
        otp.setId(UUID.randomUUID().toString());
        otp.setUser(user);
        otp.setPurpose(otpPurpose);
        otp.setVerified(false);
        otp.setOtp(OTP_CODE);
        otp.setCreatedAt(now);
        otp.setExpiresAt(now.plusSeconds(300));
        otp.setAttempts(attempts);

        OTPRepository.save(otp);

        return OtpStatus.SUCCESS;
    }
    @Transactional
    public OTP getOTP(User user) {
        return OTPRepository.findByUser(user);
    }
    @Transactional
    public OTP getOtpWithPurpose(User user,OtpPurpose purpose) {
        return OTPRepository.findByUser(user,purpose);
    }


    @Transactional
    public boolean checkOTP(String otpOriginal, String otpCheck ){
        return otpOriginal.equals(otpCheck);
    }

    @Transactional
    public OtpStatus verifyOTP(String userId, String otpCode) {

        User user = userRepository.findById(userId);

        if (user == null) {
            return OtpStatus.NOT_FOUND;
        }

        OTP otp = getOtpWithPurpose(user,OtpPurpose.REGISTRATION);

        if (otp == null) {
            return OtpStatus.NOT_FOUND;
        }

        if (!checkOTP(otp.getOtp(), otpCode)) {
            return OtpStatus.WRONG_OTP;
        }

        LocalDateTime now = LocalDateTime.now();

        otp.setVerified(true);
        otp.setModifiedAt(now);

        UserAccount account = user.getAccount();

        account.setVerified(1);
        account.setModifiedAt(now);
        account.setModifiedBy(user.getId());

        return OtpStatus.SUCCESS;
    }

    @Transactional
    public OtpStatus verifyOtpReset(String userId, String otpCode) {

        User user = userRepository.findById(userId);

        if (user == null) {
            return OtpStatus.NOT_FOUND;
        }

        OTP otp = getOtpWithPurpose(user, OtpPurpose.PASSWORD_RESET);

        if (otp == null) {
            return OtpStatus.NOT_FOUND;
        }

        if (!checkOTP(otp.getOtp(), otpCode)) {
            return OtpStatus.WRONG_OTP;
        }

        LocalDateTime now = LocalDateTime.now();

        otp.setVerified(true);
        otp.setModifiedAt(now);

        return OtpStatus.SUCCESS;
    }
    @Transactional
    public List<OTP> getAllOTP(){
        return OTPRepository.listOTP();
    }

    @Transactional
    public int updateOTP(UUID id, OtpPurpose purpose, boolean ver, int attempt) throws Exception {

        OTP otp = OTPRepository.findById(id);

        if (otp == null) {
            return 1;
        }
//        if (OTPRepository.findById(id) != null && !id.equals(dto.getId())) {
//            return 2;
//        }

        otp.setPurpose(purpose);
        otp.setVerified(ver);
        otp.setCreatedAt(LocalDateTime.now());
        if (attempt>0){
            otp.setAttempts(otp.getAttempts()+1);
        }
        return 0;
    }

    @Transactional
    public boolean deleteOTP(UUID id) {
        return OTPRepository.deleteOTP(id) > 0;
    }
}
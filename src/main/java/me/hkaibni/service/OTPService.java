package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import me.hkaibni.controller.OTPController;
import me.hkaibni.model.OTP;
import me.hkaibni.model.User;
import me.hkaibni.repository.OTPRepository;
import me.hkaibni.repository.UserRepository;
import me.hkaibni.security.TimeSec;

import java.util.Date;
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
    public OTPController.otpState createOTP(User user, String purpose, String OTP_CODE) throws Exception {

        int attempts = (int) OTPRepository.countOtpsAfter(user, TimeSec.getDailyResetTime())+1;

        if (user==null)
            return OTPController.otpState.NULL;
        if (attempts > 6)
            return OTPController.otpState.OUT_OF_ATTEMPTS;

        OTP otp = new OTP();
        otp.setUser(user);
        otp.setPurpose(purpose);
        otp.setVerified(false);
        otp.setHashedOtp(OTP_CODE);
        otp.setCreatedAt(new Date(System.currentTimeMillis()));
        otp.setAttempts(attempts);

        OTPRepository.save(otp);

        return OTPController.otpState.SUCCESS;
    }
    @Transactional
    public OTP getOTP(User user) {
        return OTPRepository.findByUser(user);
    }
    @Transactional
    public boolean checkOTP(String otpOriginal, String otpCheck ){
        return otpOriginal.equals(otpCheck);
    }
    @Transactional
    public boolean verifyUser(User user,OTP otp){
        otp.setVerified(true);
        userService.getUser(user.getSSN()).getAccount().setVerified(1);
        return true;
    }
    @Transactional
    public List<OTP> getAllOTP(){
        return OTPRepository.listOTP();
    }

    @Transactional
    public int updateOTP(UUID id,String purpose,boolean ver,int attempt) throws Exception {

        OTP otp = OTPRepository.findById(id);

        if (otp == null) {
            return 1;
        }
//        if (OTPRepository.findById(id) != null && !id.equals(dto.getId())) {
//            return 2;
//        }

        otp.setPurpose(purpose);
        otp.setVerified(ver);
        otp.setCreatedAt(new Date(System.currentTimeMillis()));
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
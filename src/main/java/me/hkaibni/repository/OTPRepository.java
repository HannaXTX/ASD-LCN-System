package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.OTP;
import me.hkaibni.model.userdata.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.count;

@ApplicationScoped
public class OTPRepository implements PanacheRepository<OTP> {

    public OTP findByUser(User us) {
        return find("user.ssn", us.getSsn()).firstResult();
    }

    public OTP findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void save(OTP otp) {
        persist(otp);
    }
    public List<OTP> listOTP(){
        return this.listAll();
    }


    public long deleteOTP(UUID id) {
        return delete("id",id);
    }
    public long countOtpsAfter(User user, Date resetTime) {
        return count(
                "user = ?1 and createdAt >= ?2",
                user,
                resetTime
        );
    }

}

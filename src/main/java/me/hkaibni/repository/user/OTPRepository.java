package me.hkaibni.repository.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.OTP;
import me.hkaibni.model.roles_types.OtpPurpose;
import me.hkaibni.model.userdata.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.count;

@ApplicationScoped
public class OTPRepository implements PanacheRepository<OTP> {

    public OTP findByUser(User us) {
        return find(
                "user.id",
                Sort.by("createdAt").descending(),
                us.getId()
        ).firstResult();
    }

    public OTP findByUser(User us, OtpPurpose purpose) {
        return find(
                "user.id = ?1 and purpose = ?2",
                Sort.by("createdAt").descending(),
                us.getId(),
                purpose
        ).firstResult();
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
    public long countOtpAfter(User user, LocalDateTime resetTime, OtpPurpose purpose) {
        return count(
                "user = ?1 and createdAt >= ?2 and purpose = ?3",
                user,
                resetTime,
                purpose
        );
    }

}

package me.hkaibni.service;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.controller.AuthController;
import me.hkaibni.dto.LoginDTO;
import me.hkaibni.model.User;
import me.hkaibni.repository.UserRepository;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.PBKDF2;
import java.sql.Date;
import java.time.Instant;


@ApplicationScoped
public class AuthService {

    enum usertype {BASIC,ADMIN}

    @Inject
    UserRepository userRepository;

    @Transactional
    public AuthController.STATE login(LoginDTO dto) throws Exception {

        User user = userRepository.findBySSN(dto.getSSN());

        if (user == null) {
            return AuthController.STATE.INVALID_CRED;
        }

        if (user.getApproved()==0){
            return AuthController.STATE.PENDING_VER;
        }

        if (user.getVerified()==0){
            return AuthController.STATE.PENDING_APR;
        }

        if (PBKDF2.validatePassword(AESUtil.decrypt(dto.getPassword()), user.getSalt(), user.getPassword())) {
            String perm = String.valueOf(user.getUserType().getPrivilege()).equals(usertype.ADMIN.toString())
                    ? "ADMIN"
                    : "USER";
            user.setLastLogged(new Date(System.currentTimeMillis()));
            String token = Jwt.issuer("hkaibni.me")
                    .subject(user.getId().toString())
                    .groups(perm)
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .sign();
            user.setToken(token);

            return AuthController.STATE.SUCCESS;
        }
        return AuthController.STATE.INVALID_CRED;
    }

}

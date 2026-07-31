package me.hkaibni.service;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.controller.AuthController;
import me.hkaibni.dto.entity_dto.LoginDTO;
import me.hkaibni.model.User;
import me.hkaibni.model.UserPanel;
import me.hkaibni.repository.UserPanelRepository;
import me.hkaibni.repository.UserRepository;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.PBKDF2;
import me.hkaibni.service.results.LoginStatus;

import java.time.Instant;
import java.time.LocalDateTime;


@ApplicationScoped
public class AuthService {


    @Inject
    UserPanelRepository userPanelRepository;
    @Inject
    UserRepository userRepository;

    @Transactional
    public LoginStatus loginUser(LoginDTO dto) throws Exception {

        User user = userRepository.findBySSN(dto.getSSN());

        if (user == null) {
            return LoginStatus.INVALID_CRED;
        }

        if (user.getAccount().getApproved()==0){
            return LoginStatus.PENDING_APR;
        }

        if (user.getAccount().getVerified()==0){
            return LoginStatus.PENDING_VER;
        }

        if (PBKDF2.validatePassword(AESUtil.decrypt(dto.getPassword()), user.getAccount().getSalt(), user.getAccount().getPassword())) {
            user.getAccount().setLastLoggedAt(LocalDateTime.now());
            String token = Jwt.issuer("hkaibni.me")
                    .subject(user.getId().toString())
                    .groups(user.getAccount().getUserType().getPrivilege())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .sign();
            user.getAccount().setToken(token);

            return LoginStatus.SUCCESS;
        }
        return LoginStatus.INVALID_CRED;
    }


    @Transactional
    public LoginStatus loginPanel(LoginDTO dto) throws Exception {

        UserPanel user = userPanelRepository.findBySSN(dto.getSSN());

        if (user == null) {
            return LoginStatus.INVALID_CRED;
        }

        if (user.getAccount().getApproved()==0){
            return LoginStatus.PENDING_APR;
        }

        if (user.getAccount().getVerified()==0){
            return LoginStatus.PENDING_VER;
        }

        if (PBKDF2.validatePassword(AESUtil.decrypt(dto.getPassword()), user.getAccount().getSalt(), user.getAccount().getPassword())) {
            user.getAccount().setLastLoggedAt(LocalDateTime.now());
            String token = Jwt.issuer("hkaibni.me")
                    .subject(user.getId().toString())
                    .groups(user.getAccount().getUserType().getPrivilege())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .sign();
            user.getAccount().setToken(token);

            return LoginStatus.SUCCESS;
        }
        return LoginStatus.INVALID_CRED;
    }

}

package me.hkaibni.service.users;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.request.LoginDTO;
import me.hkaibni.dto.request.PanelLoginDTO;
import me.hkaibni.dto.request.PasswordResetDTO;
import me.hkaibni.model.userdata.User;
import me.hkaibni.model.userdata.PanelUser;
import me.hkaibni.repository.user.PanelUserRepository;
import me.hkaibni.repository.user.UserRepository;
import me.hkaibni.security.AESUtils;
import me.hkaibni.security.PBKDF2;
import me.hkaibni.service.status.LoginStatus;
import me.hkaibni.service.status.resetPasswordStatus;

import java.time.Instant;
import java.time.LocalDateTime;


@ApplicationScoped
public class AuthService {


    @Inject
    PanelUserRepository panelUserRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    UserService userService;

    @Transactional
    public LoginStatus loginUser(LoginDTO dto) throws Exception {

        User user = userRepository.findBySSN(dto.getSsn());

        if (user == null) {
            return LoginStatus.INVALID_CRED;
        }

        String decryptedPassword = AESUtils.decryptData(dto.getPassword());

        if (decryptedPassword == null)
            return LoginStatus.INVALID_CRED;

        if (user.getAccount().getVerified()==0){
            return LoginStatus.PENDING_VER;
        }
        if (user.getAccount().getApproved()==0){
            return LoginStatus.PENDING_APR;
        }

        if (PBKDF2.validatePassword(decryptedPassword, user.getAccount().getSalt(), user.getAccount().getPassword())) {
            user.getAccount().setLastLoggedAt(LocalDateTime.now());
            String token = Jwt.issuer("hkaibni.me")
                    .subject(user.getId())
                    .groups(user.getAccount().getUserType().getPrivilege())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .sign();
            user.getAccount().setToken(token);

            return LoginStatus.SUCCESS;
        }
        return LoginStatus.INVALID_CRED;
    }


    @Transactional
    public LoginStatus loginPanel(PanelLoginDTO dto) throws Exception {

        PanelUser user = panelUserRepository.findByUsername(dto.getUsername());

        if (user == null) {
            return LoginStatus.INVALID_CRED;
        }

        if (PBKDF2.validatePassword(AESUtils.decryptData(dto.getPassword()), user.getAccount().getSalt(), user.getAccount().getPassword())) {
            user.getAccount().setLastLoggedAt(LocalDateTime.now());
            String token = Jwt.issuer("hkaibni.me")
                    .subject(user.getId())
                    .groups(user.getAccount().getUserType().getPrivilege())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .sign();
            user.getAccount().setToken(token);

            return LoginStatus.SUCCESS;
        }
        return LoginStatus.INVALID_CRED;
    }

    @Transactional
    public resetPasswordStatus updatePassword(PasswordResetDTO dto) throws Exception {
        if (dto.getNewPassword()==null) {
            return resetPasswordStatus.NULL_PASSWORD;
        }
        String decryptedPassword = AESUtils.decryptData(dto.getNewPassword());
        if (decryptedPassword==null) {
            return resetPasswordStatus.NULL_PASSWORD;
        }
        User user = userService.getUserById(dto.getUserId());
        if (user ==null){
            return resetPasswordStatus.NULL_USER;
        }
        String storedHashedPassword = user.getAccount().getPassword();
        byte[] storedSalt = user.getAccount().getSalt();

        String newHashedPassword = PBKDF2.hash(decryptedPassword,storedSalt);


        if (storedHashedPassword.equals(newHashedPassword))
            return resetPasswordStatus.SAME_PASSWORD;

        byte[] salt = PBKDF2.getSalt();

        user.getAccount().setSalt(salt);
        user.getAccount().setPassword(PBKDF2.hash(decryptedPassword,salt));
        user.getAccount().setModifiedAt(LocalDateTime.now());
        user.getAccount().setModifiedBy(user.getId());

        return resetPasswordStatus.SUCCESS;
    }

}

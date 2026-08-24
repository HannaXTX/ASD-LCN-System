package me.hkaibni.service.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.request.PanelUserCreateDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.userdata.PanelUserAccount;
import me.hkaibni.model.userdata.PanelUser;
import me.hkaibni.repository.user.PanelUserAccountRepository;
import me.hkaibni.repository.user.PanelUserRepository;
import me.hkaibni.repository.user.UserRoleRepository;
import me.hkaibni.security.AESUtils;
import me.hkaibni.security.PBKDF2;
import me.hkaibni.service.status.UpdateStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PanelUserService {

    @Inject
    PanelUserRepository panelUserRepository;
    @Inject
    PanelUserAccountRepository userAccountRepository;
    @Inject
    UserRoleRepository userRoleRepository;



    @Transactional
    public boolean createUser(PanelUserCreateDTO dto) throws Exception {

        if (panelUserRepository.findByUsername(dto.getUsername()) != null) {
            return false;
        }

        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtils.decryptData(dto.getPassword());
        LocalDateTime now = LocalDateTime.now();

        PanelUser user = new PanelUser();


        user.setId(UUID.randomUUID().toString());

        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(now);
        user.setModifiedAt(now);



        PanelUserAccount userAccount = new PanelUserAccount();
        userAccount.setId(UUID.randomUUID().toString());
        userAccount.setPassword(PBKDF2.hash(decryptedPassword, salt));
        userAccount.setSalt(salt);
        userAccount.setCreatedAt(now);
        userAccount.setModifiedAt(now);
        userAccount.setVerified(1);
        userAccount.setApproved(1);

        userAccount.setUserType(userRoleRepository.findByPrivilege("ADMIN"));

        user.setAccount(userAccount);

        panelUserRepository.save(user);
        userAccountRepository.save(userAccount);


        return true;
    }

    public PanelUser getUserPanelByUsername(String username) {
        return panelUserRepository.findByUsername(username);
    }
    public PanelUser getUserPanelById(String uuid) {
        return panelUserRepository.findById(uuid);
    }


    public List<PanelUser> getAllUserPanels(){
        return panelUserRepository.listUserPanels();
    }

    @Transactional
    public UpdateStatus updateUserPanel(String id, PanelUserCreateDTO dto) throws Exception {

        LocalDateTime now = LocalDateTime.now();


        PanelUser user = panelUserRepository.findById(id);

        if (user == null) {
            return UpdateStatus.NOT_FOUND;
        }
//        if (userRepository.findBySSN(dto.getSSN()) != null && !SSN.equals(dto.getSSN())) {
//            return 2;
//        }
        if (panelUserRepository.findByUsername(dto.getUsername()) != null) {
            return UpdateStatus.ALREADY_EXISTS;
        }
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            byte[] salt = PBKDF2.getSalt();
            user.getAccount().setPassword(PBKDF2.hash(AESUtils.decryptData(dto.getPassword()), salt));
            user.getAccount().setSalt(salt);
        }

        user.setModifiedAt(now);
        return UpdateStatus.SUCCESS;
    }

    @Transactional
    public boolean deleteUserPanelByUsername(String username) {
        return panelUserRepository.deleteByUsername(username) > 0;
    }

    @Transactional
    public boolean deleteUserPanelById(String id) {
        return panelUserRepository.deleteById(id) > 0;
    }


    @Transactional
    public int approve(String uuid) {
        getUserPanelById(uuid).getAccount().setApproved(1);
        return 0;
    }


    public List<PanelUser> searchUserPanels(UserSearchDTO request) {
        return panelUserRepository.search(request);
    }

    public List<PanelUser> searchUserPanels(String request, int page, int pageSize) {
        return panelUserRepository.search(request,page,pageSize);
    }

    //    @Transactional
    //    public int approve(String ssn) {
    //        getUser(ssn).setApproved(1);
    //        return 0;
    //    }


}
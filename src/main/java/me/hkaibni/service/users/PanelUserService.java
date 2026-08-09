package me.hkaibni.service.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.UserDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.userdata.PanelUserAccount;
import me.hkaibni.model.Address;
import me.hkaibni.model.userdata.PanelUser;
import me.hkaibni.repository.user.AddressRepository;
import me.hkaibni.repository.user.PanelUserAccountRepository;
import me.hkaibni.repository.user.PanelUserRepository;
import me.hkaibni.repository.user.UserRoleRepository;
import me.hkaibni.security.AESUtil;
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
    AddressRepository addressRepository;
    @Inject
    PanelUserAccountRepository userAccountRepository;
    @Inject
    UserRoleRepository userRoleRepository;
    @Inject
    OTPService otpService;


    @Transactional
    public boolean createUser(UserDTO dto) throws Exception {

        if (panelUserRepository.findBySsn(dto.getSsn()) != null) {
            return false;
        }

        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtil.decrypt(dto.getPassword());
        LocalDateTime now = LocalDateTime.now();

        PanelUser user = new PanelUser();


        user.setId(UUID.randomUUID().toString());

        user.setSsn(dto.getSsn());
        Address address = addressRepository.findById(dto.getAddressId());
        user.setAddress(address);
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


    public PanelUser getUserPanel(String SSN) {
        return panelUserRepository.findBySsn(SSN);
    }
    public PanelUser getUserPanel(UUID uuid) {
        return panelUserRepository.findById(uuid);
    }


    public List<PanelUser> getAllUserPanels(){
        return panelUserRepository.listUserPanels();
    }

    @Transactional
    public UpdateStatus updateUserPanel(UUID id, UserDTO dto) throws Exception {

        LocalDateTime now = LocalDateTime.now();


        PanelUser user = panelUserRepository.findById(id);

        if (user == null) {
            return UpdateStatus.NOT_FOUND;
        }
//        if (userRepository.findBySSN(dto.getSSN()) != null && !SSN.equals(dto.getSSN())) {
//            return 2;
//        }
        if (panelUserRepository.findBySsn(dto.getSsn()) != null) {
            return UpdateStatus.ALREADY_EXISTS;
        }
        user.setSsn(dto.getSsn());
        Address address = addressRepository.findById(dto.getAddressId());
        user.setAddress(address);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            byte[] salt = PBKDF2.getSalt();
            user.getAccount().setPassword(PBKDF2.hash(AESUtil.decrypt(dto.getPassword()), salt));
            user.getAccount().setSalt(salt);
        }

        user.setModifiedAt(now);
        return UpdateStatus.SUCCESS;
    }

    @Transactional
    public boolean deleteUserPanel(String SSN) {
        return panelUserRepository.deleteBySsn(SSN) > 0;
    }

    @Transactional
    public int approve(String uuid) {
        getUserPanel(uuid).getAccount().setApproved(1);
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
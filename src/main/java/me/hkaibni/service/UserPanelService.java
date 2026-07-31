package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.UserDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.Account;
import me.hkaibni.model.Address;
import me.hkaibni.model.UserPanel;
import me.hkaibni.repository.*;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.PBKDF2;
import me.hkaibni.service.results.UpdateStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserPanelService {

    @Inject
    UserPanelRepository userPanelRepository;
    @Inject
    AddressRepository addressRepository;
    @Inject
    AccountRepository accountRepository;
    @Inject
    UserTypeRepository userTypeRepository;
    @Inject
    OTPService otpService;


    @Transactional
    public boolean createUser(UserDTO dto) throws Exception {

        if (userPanelRepository.findBySSN(dto.getSSN()) != null) {
            return false;
        }

        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtil.decrypt(dto.getPassword());
        LocalDateTime now = LocalDateTime.now();

        UserPanel user = new UserPanel();



        user.setSSN(dto.getSSN());
        Address address = addressRepository.findById(dto.getAddressId());
        user.setAddress(address);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(now);
        user.setModifiedAt(now);



        Account account = new Account();
        account.setPassword(PBKDF2.hash(decryptedPassword, salt));
        account.setSalt(salt);
        account.setCreatedAt(now);
        account.setModifiedAt(now);
        account.setVerified(1);
        account.setApproved(1);

        account.setUserType(userTypeRepository.findByPrivilege("ADMIN"));

        user.setAccount(account);

        userPanelRepository.save(user);
        accountRepository.save(account);


        return true;
    }


    public UserPanel getUserPanel(String SSN) {
        return userPanelRepository.findBySSN(SSN);
    }
    public UserPanel getUserPanel(UUID uuid) {
        return userPanelRepository.findById(uuid);
    }


    public List<UserPanel> getAllUserPanels(){
        return userPanelRepository.ListUserPanels();
    }

    @Transactional
    public UpdateStatus updateUserPanel(UUID id, UserDTO dto) throws Exception {

        LocalDateTime now = LocalDateTime.now();


        UserPanel user = userPanelRepository.findById(id);

        if (user == null) {
            return UpdateStatus.NOT_FOUND;
        }
//        if (userRepository.findBySSN(dto.getSSN()) != null && !SSN.equals(dto.getSSN())) {
//            return 2;
//        }
        if (userPanelRepository.findBySSN(dto.getSSN()) != null) {
            return UpdateStatus.ALREADY_EXISTS;
        }
        user.setSSN(dto.getSSN());
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
        return userPanelRepository.deleteBySSN(SSN) > 0;
    }

    @Transactional
    public int approve(UUID uuid) {
        getUserPanel(uuid).getAccount().setApproved(1);
        return 0;
    }


    public List<UserPanel> searchUserPanels(UserSearchDTO request) {
        return userPanelRepository.search(request);
    }

    public List<UserPanel> searchUserPanels(String request,int page,int pageSize) {
        return userPanelRepository.search(request,page,pageSize);
    }

    //    @Transactional
    //    public int approve(String ssn) {
    //        getUser(ssn).setApproved(1);
    //        return 0;
    //    }


}
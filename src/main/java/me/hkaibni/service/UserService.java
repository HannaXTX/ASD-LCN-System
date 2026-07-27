package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.UserDTO;
import me.hkaibni.dto.UserSearchDTO;
import me.hkaibni.model.Account;
import me.hkaibni.model.Address;
import me.hkaibni.model.User;
import me.hkaibni.model.UserType;
import me.hkaibni.repository.AccountRepository;
import me.hkaibni.repository.AddressRepository;
import me.hkaibni.repository.UserRepository;
import me.hkaibni.repository.UserTypeRepository;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.PBKDF2;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.count;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;
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

        if (userRepository.findBySSN(dto.getSSN()) != null) {
            return false;
        }

        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtil.decrypt(dto.getPassword());
        LocalDateTime now = LocalDateTime.now();

        User user = new User();



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
        account.setVerified(0);
        account.setApproved(0);

        account.setUserType(userTypeRepository.findByPrivilege("BASIC"));

        user.setAccount(account);
        account.setUser(user);

//        Random random = new Random();
//        int number = 100000 + random.nextInt(900000);

        userRepository.save(user);
        accountRepository.save(account);

        /*
        Could USE THIS TO ALLOW AUTO CREATION ON USER PERSIST
        INSTEAD OF EXPLICITLY SAVING THE ACCOUNT TOO

            @OneToOne(
                mappedBy = "user",
                cascade = CascadeType.ALL
            )
            private Account account;

         */

        return true;
    }


    public User getUser(String SSN) {
        return userRepository.findBySSN(SSN);
    }
    public User getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }


    public List<User> getAllUsers(){
        return userRepository.ListUsers();
    }

    @Transactional
    public int updateUser(UUID id, UserDTO dto) throws Exception {

        LocalDateTime now = LocalDateTime.now();


        User user = userRepository.findById(id);

        if (user == null) {
            return 1;
        }
//        if (userRepository.findBySSN(dto.getSSN()) != null && !SSN.equals(dto.getSSN())) {
//            return 2;
//        }
        if (userRepository.findBySSN(dto.getSSN()) != null) {
            return 2;
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

        return 0;
    }

    @Transactional
    public boolean deleteUser(String SSN) {
        return userRepository.deleteBySSN(SSN) > 0;
    }

    @Transactional
    public int approve(UUID uuid) {
        getUser(uuid).getAccount().setApproved(1);
        return 0;
    }


    public List<User> searchUsers(UserSearchDTO request) {
        return userRepository.search(request);
    }

    public List<User> searchUsers(String request) {
        return userRepository.search(request);
    }

    //    @Transactional
    //    public int approve(String ssn) {
    //        getUser(ssn).setApproved(1);
    //        return 0;
    //    }


}
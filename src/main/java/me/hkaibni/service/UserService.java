package me.hkaibni.service;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.UserDTO;
import me.hkaibni.model.Address;
import me.hkaibni.model.User;
import me.hkaibni.model.UserType;
import me.hkaibni.repository.AddressRepository;
import me.hkaibni.repository.UserRepository;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.GFG;
import me.hkaibni.security.PBKDF2;

import java.sql.Date;
import java.util.List;
import java.util.Random;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.count;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;
    @Inject
    AddressRepository addressRepository;
    @Inject
    OTPService otpService;


    @Transactional
    public boolean createUser(UserDTO dto) throws Exception {

        if (userRepository.findBySSN(dto.getSSN()) != null) {
            return false;
        }

        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtil.decrypt(dto.getPassword());


        User user = new User();

        user.setSSN(dto.getSSN());
        user.setPassword(PBKDF2.hash(decryptedPassword, salt));
        user.setSalt(salt);

        Address address = addressRepository.findById(dto.getAddressId());
        user.setAddress(address);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        UserType ut = new UserType();
        user.setUserType(ut);

        ut.setId(0);
        ut.setPrivilege("BASIC");

        user.setVerified(0);
        user.setApproved(0);

        Date now = new Date(System.currentTimeMillis());

        user.setCreated(now);
        user.setModified(now);

        Random random = new Random();
        int number = 100000 + random.nextInt(900000);



        userRepository.save(user);



        return true;
    }


    public User getUser(String SSN) {
        return userRepository.findBySSN(SSN);
    }

    public List<User> getAllUsers(){
        return userRepository.ListUsers();
    }

    @Transactional
    public int updateUser(String SSN, UserDTO dto) throws Exception {

        User user = userRepository.findBySSN(SSN);

        if (user == null) {
            return 1;
        }
        if (userRepository.findBySSN(dto.getSSN()) != null && !SSN.equals(dto.getSSN())) {
            return 2;
        }
        user.setSSN(dto.getSSN());
        Address address = addressRepository.findById(dto.getAddressId());
        user.setAddress(address);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            byte[] salt = PBKDF2.getSalt();
            user.setPassword(PBKDF2.hash(AESUtil.decrypt(dto.getPassword()), salt));
            user.setSalt(salt);
        }

        user.setModified(new Date(System.currentTimeMillis()));

        return 0;
    }

    @Transactional
    public boolean deleteUser(String SSN) {
        return userRepository.deleteBySSN(SSN) > 0;
    }
    @Transactional
    public int approve(String ssn) {
        getUser(ssn).setApproved(1);
        return 0;
    }
}
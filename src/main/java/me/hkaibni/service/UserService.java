package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.UserDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.userdata.Account;
import me.hkaibni.model.Address;
import me.hkaibni.model.userdata.User;
import me.hkaibni.model.family.Person;
import me.hkaibni.repository.*;
import me.hkaibni.repository.family.PersonRepository;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.PBKDF2;
import me.hkaibni.service.results.UpdateStatus;

import java.time.LocalDateTime;
import java.util.List;
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
    @Inject
    PersonRepository personRepository;


    @Transactional
    public boolean createUser(UserDTO dto) throws Exception {

        String ssn = dto.getSsn();
        if (ssn == null || ssn.isBlank()) {
            throw new IllegalArgumentException("SSN is required");
        }

        if (userRepository.findBySSN(dto.getSsn()) != null) {
            return false;
        }


        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtil.decrypt(dto.getPassword());
        LocalDateTime now = LocalDateTime.now();

        User user = new User();



        user.setSsn(dto.getSsn());
        Address address = addressRepository.findByCode(dto.getAddressId());
        user.setAddress(address);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(now);
        user.setModifiedAt(now);

        Person person = new Person();
        person.setDateOfBirth(dto.getDateOfBirth());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setGender(dto.getGender());

        user.setPerson(person);



        Account account = new Account();
        account.setPassword(PBKDF2.hash(decryptedPassword, salt));
        account.setSalt(salt);
        account.setCreatedAt(now);
        account.setModifiedAt(now);
        account.setVerified(0);
        account.setApproved(0);

        account.setUserType(userTypeRepository.findByPrivilege("BASIC"));

        user.setAccount(account);

        personRepository.save(person);
        accountRepository.save(account);
        userRepository.save(user);


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
    public UpdateStatus updateUser(UUID id, UserDTO dto) throws Exception {

        LocalDateTime now = LocalDateTime.now();


        User user = userRepository.findById(id);

        if (user == null) {
            return UpdateStatus.NOT_FOUND;
        }
        if (userRepository.findBySSN(dto.getSsn()) != null) {
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
    public boolean deleteUser(String SSN) {
        return userRepository.deleteBySSN(SSN) > 0;
    }

    @Transactional
    public int approve(UUID uuid) {
        if (getUser(uuid)==null)
            return 1;
        getUser(uuid).getAccount().setApproved(1);
        return 0;
    }



    public List<User> searchUsers(UserSearchDTO request) {
        return userRepository.search(request);
    }

    public List<User> searchUsers(String request, int page,int pageSize) {
        return userRepository.search(request,page,pageSize);
    }

    //    @Transactional
    //    public int approve(String ssn) {
    //        getUser(ssn).setApproved(1);
    //        return 0;
    //    }


}
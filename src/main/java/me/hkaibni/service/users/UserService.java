package me.hkaibni.service.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.UserDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.userdata.UserAccount;
import me.hkaibni.model.Address;
import me.hkaibni.model.userdata.User;
import me.hkaibni.model.family.Person;
import me.hkaibni.repository.family.PersonRepository;
import me.hkaibni.repository.user.AddressRepository;
import me.hkaibni.repository.user.UserAccountRepository;
import me.hkaibni.repository.user.UserRepository;
import me.hkaibni.repository.user.UserRoleRepository;
import me.hkaibni.security.AESUtil;
import me.hkaibni.security.PBKDF2;
import me.hkaibni.service.status.UpdateStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

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
    UserAccountRepository userAccountRepository;
    @Inject
    UserRoleRepository userRoleRepository;
    @Inject
    OTPService otpService;
    @Inject
    PersonRepository personRepository;
    @Inject
    JsonWebToken token;


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


        user.setId(UUID.randomUUID().toString());
        user.setSsn(dto.getSsn());
        Address address = addressRepository.findByCode(dto.getAddressId());
        user.setAddress(address);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(now);
        user.setModifiedAt(now);

        Person person = new Person();
        person.setId(UUID.randomUUID().toString());
        person.setDateOfBirth(dto.getDateOfBirth());
        person.setFirstNameEn(dto.getFirstNameEn());
        person.setLastNameEn(dto.getLastNameEn());
        person.setGender(dto.getGender());

        person.setCreatedAt(now);

        user.setPerson(person);




        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID().toString());
        userAccount.setPassword(PBKDF2.hash(decryptedPassword, salt));
        userAccount.setSalt(salt);
        userAccount.setCreatedAt(now);
        userAccount.setModifiedAt(now);
        userAccount.setVerified(0);
        userAccount.setApproved(0);

        userAccount.setUserType(userRoleRepository.findByPrivilege("BASIC"));

        user.setAccount(userAccount);

        personRepository.save(person);
        userAccountRepository.save(userAccount);
        userRepository.save(user);
        person.setCreatedBy(user.getId());


        return true;
    }


    public User getUserBySsn(String SSN) {
        return userRepository.findBySSN(SSN);
    }
    public User getUserById(String id) {
        return userRepository.findById(id);
    }


    public List<User> getAllUsers(){
        return userRepository.listUsers();
    }

    @Transactional
    public UpdateStatus updateUser(String id, UserDTO dto) throws Exception {

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
        user.setModifiedBy(token.getSubject());

        return UpdateStatus.SUCCESS;
    }

    @Transactional
    public boolean deleteUser(String SSN) {
        return userRepository.deleteBySSN(SSN) > 0;
    }

    @Transactional
    public int approve(String uuid) {
        if (getUserById(uuid)==null)
            return 1;
        getUserById(uuid).getAccount().setApproved(1);
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
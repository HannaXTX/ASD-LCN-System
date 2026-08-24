package me.hkaibni.service.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.request.UserDTO;
import me.hkaibni.dto.request.UserUpdateDTO;
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
import me.hkaibni.security.AESUtils;
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
    public User createUser(UserDTO dto) throws Exception {

        String ssn = dto.getSsn();

        if (ssn == null || ssn.isBlank()) {
            throw new IllegalArgumentException("SSN is required");
        }

        if (userRepository.findBySSN(dto.getSsn()) != null) {
            return null;
        }


        byte[] salt = PBKDF2.getSalt();

        String decryptedPassword = AESUtils.decryptData(dto.getPassword());
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

        userAccount.setUserType(userRoleRepository.findByPrivilege("USER"));

        user.setAccount(userAccount);

        personRepository.save(person);
        userAccountRepository.save(userAccount);
        userRepository.save(user);
        person.setCreatedBy(user.getId());


        return user;
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
    public UpdateStatus updateUser(String id, UserUpdateDTO dto) throws Exception {

        User user = userRepository.findById(id);

        if (user == null) {
            return UpdateStatus.NOT_FOUND;
        }

        LocalDateTime now = LocalDateTime.now();
        String modifiedBy = token.getSubject();

        boolean userChanged = false;
        boolean personChanged = false;
        boolean accountChanged = false;

//        // SSN
//        if (dto.getSsn() != null
//                && !dto.getSsn().isBlank()
//                && !dto.getSsn().equals(user.getSsn())) {
//
//            User existingUser = userRepository.findBySSN(dto.getSsn());
//
//            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
//                return UpdateStatus.ALREADY_EXISTS;
//            }
//
//            user.setSsn(dto.getSsn());
//            userChanged = true;
//        }

        // Phone
        if (dto.getPhone() != null
                && !dto.getPhone().equals(user.getPhone())) {

            user.setPhone(dto.getPhone());
            userChanged = true;
        }

        // Email
        if (dto.getEmail() != null
                && !dto.getEmail().equals(user.getEmail())) {

            user.setEmail(dto.getEmail());
            userChanged = true;
        }

        // Address
//        if (dto.getAddressId() != null
//                && !dto.getAddressId().isBlank()) {
//
//            Address address = addressRepository.findByCode(dto.getAddressId());
//
//            if (address == null) {
//                throw new IllegalArgumentException("Invalid address");
//            }
//
//            if (user.getAddress() == null
//                    || !address.getId().equals(user.getAddress().getId())) {
//
//                user.setAddress(address);
//                userChanged = true;
//            }
//        }


        Person person = user.getPerson();

        if (person != null) {

            if (dto.getFirstNameEn() != null
                    && !dto.getFirstNameEn().equals(person.getFirstNameEn())) {

                person.setFirstNameEn(dto.getFirstNameEn());
                personChanged = true;
            }
            if (dto.getFirstNameAr() != null
                    && !dto.getFirstNameAr().equals(person.getFirstNameAr())) {

                person.setFirstNameAr(dto.getFirstNameAr());
                personChanged = true;
            }
            if (dto.getMiddleNameAr() != null
                    && !dto.getMiddleNameAr().equals(person.getMiddleNameAr())) {

                person.setMiddleNameAr(dto.getMiddleNameAr());
                personChanged = true;
            }

            if (dto.getMiddleNameEn() != null
                    && !dto.getMiddleNameEn().equals(person.getMiddleNameEn())) {

                person.setMiddleNameEn(dto.getMiddleNameEn());
                personChanged = true;
            }

            if (dto.getLastNameAr() != null
                    && !dto.getLastNameAr().equals(person.getLastNameAr())) {

                person.setLastNameAr(dto.getLastNameAr());
                personChanged = true;
            }

            if (dto.getLastNameEn() != null
                    && !dto.getLastNameEn().equals(person.getLastNameEn())) {

                person.setLastNameEn(dto.getLastNameEn());
                personChanged = true;
            }

            if (dto.getDateOfBirth() != null
                    && !dto.getDateOfBirth().equals(person.getDateOfBirth())) {

                person.setDateOfBirth(dto.getDateOfBirth());
                personChanged = true;
            }

            if (dto.getGender() != null
                    && !dto.getGender().equals(person.getGender())) {

                person.setGender(dto.getGender());
                personChanged = true;
            }
        }

        UserAccount account = user.getAccount();

//        if (dto.getPassword() != null
//                && !dto.getPassword().isBlank()) {
//
//            String decryptedPassword = AESUtil.decrypt(dto.getPassword());
//
//            byte[] salt = PBKDF2.getSalt();
//
//            account.setPassword(
//                    PBKDF2.hash(decryptedPassword, salt)
//            );
//
//            account.setSalt(salt);
//
//            accountChanged = true;
//        }


        if (userChanged) {
            user.setModifiedAt(now);
            user.setModifiedBy(modifiedBy);
        }

        if (personChanged) {
            person.setModifiedAt(now);
            person.setModifiedBy(modifiedBy);
        }

        if (accountChanged) {
            account.setModifiedAt(now);
            account.setModifiedBy(modifiedBy);
        }

        return UpdateStatus.SUCCESS;
    }

    @Transactional
    public boolean deleteUser(String SSN) {
        return userRepository.deleteBySSN(SSN) > 0;
    }

    @Transactional
    public int approve(String uuid) {
        User user=getUserById(uuid);
        if (user==null)
            return 1;
        user.getAccount().setApproved(1);
        user.getAccount().setModifiedAt(LocalDateTime.now());
        user.getAccount().setModifiedBy(token.getSubject());
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
    @Transactional
    public Person updateProfilePicture(User us, String attachmentId){
        User user = userRepository.findById(us.getId());
        Person person = user.getPerson();
        person.setProfilePicture(attachmentId);
        return person;
    }
}
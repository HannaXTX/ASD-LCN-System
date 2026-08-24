package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.request.PersonDTO;
import me.hkaibni.model.family.Person;
import me.hkaibni.repository.family.PersonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonService {

    @Inject
    PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.listAll();
    }

    public Person getPerson(String id) {
        return personRepository.findById(id);
    }

    @Transactional
    public Person createPerson(PersonDTO dto) {

        Person person = new Person();

        person.setId(UUID.randomUUID().toString());

        person.setFirstNameEn(dto.getFirstNameEn());
        person.setFirstNameAr(dto.getFirstNameAr());

        person.setMiddleNameEn(dto.getMiddleNameEn());
        person.setMiddleNameAr(dto.getMiddleNameAr());

        person.setLastNameEn(dto.getLastNameEn());
        person.setLastNameAr(dto.getLastNameAr());

        person.setFullNameEn(
                buildFullName(
                        dto.getFirstNameEn(),
                        dto.getMiddleNameEn(),
                        dto.getLastNameEn()
                )
        );

        person.setFullNameAr(
                buildFullName(
                        dto.getFirstNameAr(),
                        dto.getMiddleNameAr(),
                        dto.getLastNameAr()
                )
        );

        person.setDateOfBirth(dto.getDateOfBirth());
        person.setDateOfDeath(dto.getDateOfDeath());

        person.setGender(dto.getGender());
        person.setProfilePicture(dto.getProfilePicture());

        person.setCreatedAt(LocalDateTime.now());

        personRepository.persist(person);

        return person;
    }

    @Transactional
    public Person updatePerson(Person person, PersonDTO dto) {

        person.setFirstNameEn(dto.getFirstNameEn());
        person.setFirstNameAr(dto.getFirstNameAr());

        person.setMiddleNameEn(dto.getMiddleNameEn());
        person.setMiddleNameAr(dto.getMiddleNameAr());

        person.setLastNameEn(dto.getLastNameEn());
        person.setLastNameAr(dto.getLastNameAr());

        person.setFullNameEn(
                buildFullName(
                        dto.getFirstNameEn(),
                        dto.getMiddleNameEn(),
                        dto.getLastNameEn()
                )
        );

        person.setFullNameAr(
                buildFullName(
                        dto.getFirstNameAr(),
                        dto.getMiddleNameAr(),
                        dto.getLastNameAr()
                )
        );

        person.setDateOfBirth(dto.getDateOfBirth());
        person.setDateOfDeath(dto.getDateOfDeath());

        person.setGender(dto.getGender());
        person.setProfilePicture(dto.getProfilePicture());

        person.setModifiedAt(LocalDateTime.now());

        return person;
    }

    @Transactional
    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }

    private String buildFullName(
            String firstName,
            String middleName,
            String lastName
    ) {

        return String.join(" ",
                        firstName == null ? "" : firstName,
                        middleName == null ? "" : middleName,
                        lastName == null ? "" : lastName
                )
                .trim()
                .replaceAll("\\s+", " ");
    }
}
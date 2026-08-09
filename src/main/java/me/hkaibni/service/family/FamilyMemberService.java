package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.FamilyDTO;
import me.hkaibni.dto.entity_dto.FamilyMemberDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.family.FamilyMember;
import me.hkaibni.model.family.Person;
import me.hkaibni.repository.family.FamilyMemberRepository;
import me.hkaibni.repository.family.FamilyRepository;
import me.hkaibni.repository.family.PersonRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FamilyMemberService {

    @Inject
    FamilyMemberRepository familyMemberRepository;
    @Inject
    FamilyRepository familyRepository;
    @Inject
    PersonRepository personRepository;

    @Transactional
    public int createFamilyMember(FamilyMemberDTO dto) {

        if (dto == null) {
            return 1;
        }
        Family family = familyRepository.findFamilyById(dto.getFamily());
        Person person = personRepository.findById(dto.getPerson());

        if (person == null ||
                family== null) {
            return 1;
        }

        if (familyMemberRepository.findByPersonAndFamily(person,family)!= null) {
            return 3;
        }

        FamilyMember familyMember = new FamilyMember();

        familyMember.setId(UUID.randomUUID().toString());
        familyMember.setFamily(familyRepository.findFamilyById(dto.getFamily()));
        familyMember.setPerson(personRepository.findById(dto.getPerson()));
        familyMember.setRootPerson(dto.isRootPerson());

        familyMemberRepository.save(familyMember);

        return 0;
    }

    public Family getFamilyById(UUID id) {
        return familyRepository.findFamilyById(id);
    }


    public Family getFamilyByNameEn(String familyName) {
        return familyRepository.findByNameEn(familyName);
    }
    public Family getFamilyByNameAr(String familyName) {
        return familyRepository.findByNameAr(familyName);
    }


    public List<Family> getAllFamilies() {
        return familyRepository.listFamilies();
    }

    public List<Family> searchFamilies(String familyName) {
        return familyRepository.searchByNameEn(familyName);
    }

    @Transactional
    public int updateFamily(UUID id, FamilyDTO dto) {

        Family family = familyRepository.findFamilyById(id);

        if (family == null) {
            return 1;
        }

        Family existingByName =
                familyRepository.findByNameEn(dto.getNameEn());

        if (existingByName != null &&
                !existingByName.getId().equals(family.getId())) {
            return 2;
        }

        family.setNameEn(dto.getNameEn());
        family.setNameAr(dto.getNameAr());

        return 0;
    }

    @Transactional
    public boolean deleteFamilyById(UUID id) {
        return familyRepository.deleteFamilyById(id)>0;
    }

}
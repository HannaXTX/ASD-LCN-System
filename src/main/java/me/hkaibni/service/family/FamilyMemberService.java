package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.request.FamilyDTO;
import me.hkaibni.dto.request.FamilyMemberDTO;
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
    public FamilyMember createFamilyMember(FamilyMemberDTO dto, Family family, Person person) {

        FamilyMember familyMember = new FamilyMember();

        familyMember.setId(UUID.randomUUID().toString());
        familyMember.setFamily(family);
        familyMember.setPerson(person);
        familyMember.setRootPerson(dto.isRootPerson());

        familyMemberRepository.save(familyMember);

        return familyMember;
    }



    public Family getFamilyById(String id) {
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
    public int updateFamily(String id, FamilyDTO dto) {

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
    public boolean deleteFamilyById(String id) {
        return familyRepository.deleteFamilyById(id)>0;
    }

}
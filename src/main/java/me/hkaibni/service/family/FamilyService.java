package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.FamilyDTO;
import me.hkaibni.dto.response.FamilyTreeResponse;
import me.hkaibni.dto.search.FamilySearchDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.repository.family.FamilyMemberRepository;
import me.hkaibni.repository.family.FamilyRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FamilyService {

    @Inject
    FamilyRepository familyRepository;
    @Inject
    FamilyMemberRepository familyMemberRepository;

    @Transactional
    public int createFamily(FamilyDTO dto) {

        if (dto == null) {
            return 1;
        }

        if (familyRepository.findByNameAr(dto.getNameAr()) != null &&
                familyRepository.findByNameEn(dto.getNameEn()) != null) {
            return 3;
        }


        Family family = new Family();

        family.setId(UUID.randomUUID().toString());
        family.setNameAr(dto.getNameAr());
        family.setNameEn(dto.getNameEn());



        familyRepository.save(family);

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

    public List<Family> searchFamiliesEn(String familyName) {
        return familyRepository.searchByNameEn(familyName);
    }


    public List<Family> searchFamilies(FamilySearchDTO request) {
        return familyRepository.search(request);
    }

    public List<Family> searchFamilies(String request, int page,int pageSize) {
        return familyRepository.search(request,page,pageSize);
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

    public FamilyTreeResponse buildTree(UUID id) {
        return familyMemberRepository.buildTree(id);
    }
}
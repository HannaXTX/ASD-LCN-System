package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.FamilyDTO;
import me.hkaibni.model.familyTree.Family;
import me.hkaibni.repository.FamilyRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FamilyService {

    @Inject
    FamilyRepository familyRepository;

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

    public List<Family> searchFamilies(String familyName) {
        return familyRepository.searchByName(familyName);
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
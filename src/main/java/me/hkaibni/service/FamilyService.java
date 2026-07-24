package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.FamilyDTO;
import me.hkaibni.model.Family;
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

        if (familyRepository.findBySSN(dto.getSSN()) != null) {
            return 2;
        }

        if (familyRepository.findByName(dto.getFamilyName()) != null) {
            return 3;
        }

        Family family = new Family();

        family.setSSN(dto.getSSN());
        family.setFamilyName(dto.getFamilyName());

        familyRepository.save(family);

        return 0;
    }

    public Family getFamilyById(UUID id) {
        return familyRepository.findFamilyById(id);
    }

    public Family getFamilyBySSN(String ssn) {
        return familyRepository.findBySSN(ssn);
    }

    public Family getFamilyByName(String familyName) {
        return familyRepository.findByName(familyName);
    }

    public List<Family> getAllFamilies() {
        return familyRepository.listFamilies();
    }

    public List<Family> searchFamilies(String familyName) {
        return familyRepository.searchByName(familyName);
    }

    @Transactional
    public int updateFamily(String ssn, FamilyDTO dto) {

        Family family = familyRepository.findBySSN(ssn);

        if (family == null) {
            return 1;
        }

        Family existingByName =
                familyRepository.findByName(dto.getFamilyName());

        if (existingByName != null &&
                !existingByName.getId().equals(family.getId())) {
            return 2;
        }

        Family existingBySSN =
                familyRepository.findBySSN(dto.getSSN());

        if (existingBySSN != null &&
                !existingBySSN.getId().equals(family.getId())) {
            return 3;
        }

        family.setSSN(dto.getSSN());
        family.setFamilyName(dto.getFamilyName());

        return 0;
    }

    @Transactional
    public boolean deleteFamilyById(UUID id) {
        return familyRepository.deleteByFamilyId(id);
    }

    @Transactional
    public boolean deleteFamilyBySSN(String ssn) {
        return familyRepository.deleteBySSN(ssn) > 0;
    }
}
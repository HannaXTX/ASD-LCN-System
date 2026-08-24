package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.FamilyDTO;
import me.hkaibni.dto.response.FamilyTreeResponse;
import me.hkaibni.dto.search.FamilySearchDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.repository.family.FamilyMemberRepository;
import me.hkaibni.repository.family.FamilyRepository;
import me.hkaibni.utils.ResponseUtil;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FamilyService {

    @Inject
    FamilyRepository familyRepository;
    @Inject
    FamilyMemberRepository familyMemberRepository;

    @Transactional
    public Family createFamily(FamilyDTO dto) {

        Family family = new Family();

        family.setId(UUID.randomUUID().toString());
        family.setNameAr(dto.getNameAr());
        family.setNameEn(dto.getNameEn());

        familyRepository.save(family);

        return family;
    }

    public boolean isUnique(FamilyDTO dto){
        return familyRepository.findByNameAr(dto.getNameAr()) == null ||
                familyRepository.findByNameEn(dto.getNameEn()) == null;
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
    public Family updateFamily(String id, FamilyDTO dto) {
        Family family = familyRepository.findFamilyById(id);
        if (family==null) {
            return null;
        }

        family.setNameEn(dto.getNameEn());
        family.setNameAr(dto.getNameAr());

        return family;
    }

    @Transactional
    public Family deleteFamilyById(String id) {
        Family family = familyRepository.findFamilyById(id);
        familyRepository.deleteFamilyById(id);
        return family;

    }

    public FamilyTreeResponse buildTree(String id) {
        return familyMemberRepository.buildTree(id);
    }
}
package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.familyTree.Family;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FamilyRepository
        implements PanacheRepositoryBase<Family, UUID> {

    public Family findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Family findByNameAr(String familyName) {
        return find("name_ar", familyName).firstResult();
    }

    public Family findByNameEn(String familyName) {
        return find("name_en", familyName).firstResult();
    }


    public void save(Family family) {
        persist(family);
    }

    public List<Family> listFamilies() {
        return listAll();
    }


    public long deleteFamilyById(UUID id) {
        return delete("id", id);
    }

    public Family findFamilyById(UUID id) {
        return findById(id);
    }

    public List<Family> searchByName(String familyName) {
        return find(
                "LOWER(name_en) LIKE ?1",
                "%" + familyName.toLowerCase().trim() + "%"
        ).list();
    }

    public List<Family> search(String value) {
        String search = "%" + value.toLowerCase() + "%";

        return list(
                "LOWER(name_ar) LIKE ?1 OR LOWER(name_en) LIKE ?1",
                search
        );
    }

}
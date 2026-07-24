package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.Family;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FamilyRepository
        implements PanacheRepositoryBase<Family, UUID> {

    public Family findBySSN(String ssn) {
        return find("SSN", ssn).firstResult();
    }

    public Family findByName(String familyName) {
        return find("familyName", familyName).firstResult();
    }

    public void save(Family family) {
        persist(family);
    }

    public List<Family> listFamilies() {
        return listAll();
    }

    public boolean deleteByFamilyId(UUID id) {
        return deleteById(id);
    }

    public long deleteBySSN(String ssn) {
        return delete("SSN", ssn);
    }

    public Family findFamilyById(UUID id) {
        return findById(id);
    }

    public List<Family> searchByName(String familyName) {
        return find(
                "LOWER(familyName) LIKE ?1",
                "%" + familyName.toLowerCase().trim() + "%"
        ).list();
    }

    public List<Family> globalSearch(String value) {
        String search = "%" + value.toLowerCase() + "%";

        return list(
                "LOWER(familyName) LIKE ?1 OR LOWER(SSN) LIKE ?1",
                search
        );
    }

}
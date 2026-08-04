package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.FamilySearchDTO;
import me.hkaibni.model.family.Family;

import java.util.*;

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

    public List<Family> searchByNameEn(String familyName) {
        return find(
                "LOWER(name_en) LIKE ?1",
                "%" + familyName.toLowerCase().trim() + "%"
        ).list();
    }

//    public List<Family> search(String value) {
//        String search = "%" + value.toLowerCase() + "%";
//
//        return list(
//                "LOWER(name_ar) LIKE ?1 OR LOWER(name_en) LIKE ?1",
//                search
//        );
//    }


    public List<Family> search(String value,Integer page,Integer pageSize){

        StringBuilder query = new StringBuilder("1=1");
        Map<String,Object> params = new HashMap<>();

        if (value!=null && !value.isBlank()){
            String search = "%" + value.trim().toLowerCase() + "%";

            query.append("""
                            AND (lower(name_ar) like :search
                            or lower(name_en) like :search)
                            """);
            params.put("search",search);



        }

        var panacheQuery = find(query.toString(),params);

        if (page !=null && page!=-1){
            return panacheQuery.list();
        }
        int resolvedPage = page == null ? 1 : Math.max(page, 1);
        int resolvedPageSize = page == null ? 20 : Math.clamp(pageSize,1,100);

        return panacheQuery.page(Page.of(resolvedPage -1,resolvedPageSize))
                .list();
    }

    public List<Family> search(FamilySearchDTO request){



        return null;
    }

}
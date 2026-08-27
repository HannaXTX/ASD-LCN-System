package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.FamilySearchDTO;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.userdata.User;

import java.util.*;

@ApplicationScoped
public class FamilyRepository
        implements PanacheRepositoryBase<Family, String> {

    public Family findById(String id) {
        return find("id", id).firstResult();
    }

    public Family findByNameAr(String familyName) {
        return find("nameAr", familyName).firstResult();
    }

    public Family findByNameEn(String familyName) {
        return find("nameEn", familyName).firstResult();
    }


    public void save(Family family) {
        persist(family);
    }

    public List<Family> listFamilies() {
        return listAll();
    }


    public long deleteFamilyById(String id) {
        return delete("id", id);
    }

    public Family findFamilyById(String id) {
        return findById(id);
    }

    public List<Family> searchByNameEn(String familyName) {
        return find(
                "LOWER(nameEn) LIKE ?1",
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
                            AND (lower(nameEn) like :search
                            or lower(nameAr) like :search)
                            """);
            params.put("search",search);



        }

        var panacheQuery = find(query.toString(), params);

        if (page != null && page == -1) {
            return panacheQuery.list();
        }

        int resolvedPage = page == null ? 1 : Math.max(page, 1);
        int resolvedPageSize = pageSize == null ? 20 : Math.clamp(pageSize, 1, 100);

        return panacheQuery
                .page(Page.of(resolvedPage - 1, resolvedPageSize))
                .list();
    }

    public List<Family> search(FamilySearchDTO request){


            StringBuilder query = new StringBuilder("1 = 1");
            Map<String, Object> params = new HashMap<>();



            if (request.getNameAr() != null &&
                    !request.getNameAr().isBlank()) {

                query.append(" and lower(nameAr) = :nameAr");
                params.put(
                        "nameAr",
                        request.getNameAr().trim().toLowerCase()
                );
            }

            if (request.getNameEn() != null &&
                    !request.getNameEn().isBlank()) {

                query.append(" and lower(nameEn) = :nameEn");
                params.put(
                        "nameEn",
                        request.getNameEn().trim().toLowerCase()
                );
            }


            var panacheQuery = find(query.toString(), params);

            if (request.getPage() != null && request.getPage() == -1) {
                return panacheQuery.list();
            }

            int page = request.getPage() == null
                    ? 1
                    : Math.max(request.getPage(), 1);

            int pageSize = request.getPageSize() == null
                    ? 20
                    : Math.clamp(request.getPageSize(), 1, 100);

            return panacheQuery
                    .page(Page.of(page - 1, pageSize))
                    .list();
        }




}
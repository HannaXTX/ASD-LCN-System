package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.PersonSearchDTO;
import me.hkaibni.model.family.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public List<Person> findByFirstName(String firstname) {
        return list("firstname", firstname);
    }
    public List<Person> findByLastName(String lastname) {
        return list("lastname", lastname);
    }

    public Person findById(String id) {
        return find("id", id).firstResult();
    }

    public void save(Person person) {
        persist(person);
    }

    public List<Person> ListPersons(){
        return this.listAll();
    }

    public long deleteById(String id) {
        return delete("id", id);
    }

    public List<Person> search(String value, Integer page, Integer pageSize) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (value != null && !value.isBlank()) {

            String search = "%" + value.trim().toLowerCase() + "%";

            query.append("""
                and (
                    lower(firstNameEn) like :search
                    or lower(firstNameAr) like :search
                    or lower(middleNameEn) like :search
                    or lower(middleNameAr) like :search
                    or lower(lastNameEn) like :search
                    or lower(lastNameAr) like :search
                    or lower(fullNameEn) like :search
                    or lower(fullNameAr) like :search
                    or lower(id) like :search
                )
                """);

            params.put("search", search);
        }

        var panacheQuery = find(query.toString(), params);

        if (page != null && page == -1) {
            return panacheQuery.list();
        }

        int resolvedPage = page == null
                ? 1
                : Math.max(page, 1);

        int resolvedPageSize = pageSize == null
                ? 20
                : Math.clamp(pageSize, 1, 100);

        return panacheQuery
                .page(Page.of(resolvedPage - 1, resolvedPageSize))
                .list();
    }

    public List<Person> search(PersonSearchDTO request) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (request.getFirstNameEn() != null &&
                !request.getFirstNameEn().isBlank()) {

            query.append(" and lower(firstNameEn) like :firstNameEn");

            params.put(
                    "firstNameEn",
                    "%" + request.getFirstNameEn().trim().toLowerCase() + "%"
            );
        }

        if (request.getFirstNameAr() != null &&
                !request.getFirstNameAr().isBlank()) {

            query.append(" and lower(firstNameAr) like :firstNameAr");

            params.put(
                    "firstNameAr",
                    "%" + request.getFirstNameAr().trim().toLowerCase() + "%"
            );
        }

        if (request.getMiddleNameEn() != null &&
                !request.getMiddleNameEn().isBlank()) {

            query.append(" and lower(middleNameEn) like :middleNameEn");

            params.put(
                    "middleNameEn",
                    "%" + request.getMiddleNameEn().trim().toLowerCase() + "%"
            );
        }

        if (request.getMiddleNameAr() != null &&
                !request.getMiddleNameAr().isBlank()) {

            query.append(" and lower(middleNameAr) like :middleNameAr");

            params.put(
                    "middleNameAr",
                    "%" + request.getMiddleNameAr().trim().toLowerCase() + "%"
            );
        }

        if (request.getLastNameEn() != null &&
                !request.getLastNameEn().isBlank()) {

            query.append(" and lower(lastNameEn) like :lastNameEn");

            params.put(
                    "lastNameEn",
                    "%" + request.getLastNameEn().trim().toLowerCase() + "%"
            );
        }

        if (request.getLastNameAr() != null &&
                !request.getLastNameAr().isBlank()) {

            query.append(" and lower(lastNameAr) like :lastNameAr");

            params.put(
                    "lastNameAr",
                    "%" + request.getLastNameAr().trim().toLowerCase() + "%"
            );
        }

        if (request.getFullNameEn() != null &&
                !request.getFullNameEn().isBlank()) {

            query.append(" and lower(fullNameEn) like :fullNameEn");

            params.put(
                    "fullNameEn",
                    "%" + request.getFullNameEn().trim().toLowerCase() + "%"
            );
        }

        if (request.getFullNameAr() != null &&
                !request.getFullNameAr().isBlank()) {

            query.append(" and lower(fullNameAr) like :fullNameAr");

            params.put(
                    "fullNameAr",
                    "%" + request.getFullNameAr().trim().toLowerCase() + "%"
            );
        }

        if (request.getGender() != null) {
            query.append(" and gender = :gender");
            params.put("gender", request.getGender());
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
package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.family.Person;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Person findByFirstName(String firstname) {
        return find("firstname", firstname).firstResult();
    }
    public Person findByLastName(String lastname) {
        return find("lastname", lastname).firstResult();
    }

    public Person findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void save(Person person) {
        persist(person);
    }
    public List<Person> ListPersons(){
        return this.listAll();
    }
    public long deleteById(UUID id) {
        return delete("id", id);
    }

//    public List<Person> search(String value, Integer page, Integer pageSize) {
//
//        StringBuilder query = new StringBuilder("1 = 1");
//        Map<String, Object> params = new HashMap<>();
//
//        if (value != null && !value.isBlank()) {
//            String search = "%" + value.trim().toLowerCase() + "%";
//
//            query.append("""
//                and (
//                    lower(firstName) like :search
//                    or lower(lastName) like :search
//                    or lower(email) like :search
//                    or lower(SSN) like :search
//                    or lower(phone) like :search
//                )
//                """);
//
//            params.put("search", search);
//        }
//
//        var panacheQuery = find(query.toString(), params);
//
//        if (page != null && page == -1) {
//            return panacheQuery.list();
//        }
//
//        int resolvedPage = page == null
//                ? 1
//                : Math.max(page, 1);
//
//        int resolvedPageSize = pageSize == null
//                ? 20
//                : Math.clamp(pageSize, 1, 100);
//
//        return panacheQuery
//                .page(Page.of(resolvedPage - 1, resolvedPageSize))
//                .list();
//    }
//
//    public List<Person> search(PersonSearchDTO request) {
//
//        StringBuilder query = new StringBuilder("1 = 1");
//        Map<String, Object> params = new HashMap<>();
//
//        if (request.getSSN() != null && !request.getSSN().isBlank()) {
//            query.append(" and lower(SSN) like :ssn");
//            params.put(
//                    "ssn",
//                    "%" + request.getSSN().trim().toLowerCase() + "%"
//            );
//        }
//
//        if (request.getFirstName() != null &&
//                !request.getFirstName().isBlank()) {
//
//            query.append(" and lower(firstName) like :firstName");
//            params.put(
//                    "firstName",
//                    "%" + request.getFirstName().trim().toLowerCase() + "%"
//            );
//        }
//
//        if (request.getLastName() != null &&
//                !request.getLastName().isBlank()) {
//
//            query.append(" and lower(lastName) like :lastName");
//            params.put(
//                    "lastName",
//                    "%" + request.getLastName().trim().toLowerCase() + "%"
//            );
//        }
//
//        if (request.getEmail() != null &&
//                !request.getEmail().isBlank()) {
//
//            query.append(" and lower(email) = :email");
//            params.put(
//                    "email",
//                    request.getEmail().trim().toLowerCase()
//            );
//        }
//
//        if (request.getPhone() != null &&
//                !request.getPhone().isBlank()) {
//
//            query.append(" and phone like :phone");
//            params.put(
//                    "phone",
//                    "%" + request.getPhone().trim() + "%"
//            );
//        }
//
//        if (request.getAddressId() != null &&
//                !request.getAddressId().isBlank()) {
//
//            query.append(" and address.id = :addressId");
//            params.put(
//                    "addressId",
//                    request.getAddressId().trim()
//            );
//        }
//
//        var panacheQuery = find(query.toString(), params);
//
//        if (request.getPage() != null && request.getPage() == -1) {
//            return panacheQuery.list();
//        }
//
//        int page = request.getPage() == null
//                ? 1
//                : Math.max(request.getPage(), 1);
//
//        int pageSize = request.getPageSize() == null
//                ? 20
//                : Math.clamp(request.getPageSize(), 1, 100);
//
//        return panacheQuery
//                .page(Page.of(page - 1, pageSize))
//                .list();
//    }



}
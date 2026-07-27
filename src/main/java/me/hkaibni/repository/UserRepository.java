package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.UserSearchDTO;
import me.hkaibni.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findBySSN(String SSN) {
        return find("SSN", SSN).firstResult();
    }
    public User findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void save(User user) {
        persist(user);
    }
    public List<User> ListUsers(){
        return this.listAll();
    }
    public long deleteBySSN(String SSN) {
        return delete("SSN", SSN);
    }

    public List<User> search(String value) {
            String search = "%" + value.toLowerCase() + "%";

            return list(
                    "LOWER(firstName) LIKE ?1 " +
                            "OR LOWER(lastName) LIKE ?1 " +
                            "OR LOWER(email) LIKE ?1 " +
                            "OR LOWER(SSN) LIKE ?1 " +
                            "OR LOWER(phone) LIKE ?1",
                    search
            );
        }


    public List<User> search(UserSearchDTO request) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (request.getSSN() != null && !request.getSSN().isBlank()) {
            query.append(" and lower(SSN) like :ssn");
            params.put(
                    "ssn",
                    "%" + request.getSSN().trim().toLowerCase() + "%"
            );
        }

        if (request.getFirstName() != null
                && !request.getFirstName().isBlank()) {

            query.append(" and lower(firstName) like :firstName");
            params.put(
                    "firstName",
                    "%" + request.getFirstName().trim().toLowerCase() + "%"
            );
        }

        if (request.getLastName() != null
                && !request.getLastName().isBlank()) {

            query.append(" and lower(lastName) like :lastName");
            params.put(
                    "lastName",
                    "%" + request.getLastName().trim().toLowerCase() + "%"
            );
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            query.append(" and lower(email) = :email");
            params.put(
                    "email",
                    request.getEmail().trim().toLowerCase()
            );
        }

        if (request.getPhone() != null
                && !request.getPhone().isBlank()) {

            query.append(" and phone like :phone");
            params.put(
                    "phone",
                    "%" + request.getPhone().trim() + "%"
            );
        }

        if (request.getAddressId() != null
                && !request.getAddressId().isBlank()) {

            query.append(" and address.id = :addressId");
            params.put(
                    "addressId",
                    request.getAddressId().trim()
            );
        }

        int page = request.getPage() == null
                ? 1
                : Math.max(request.getPage(), 1);

        int pageSize = request.getPageSize() == null
                ? 20
                : Math.clamp(request.getPageSize(), 1, 100);

        return find(query.toString(), params)
                .page(Page.of(page, pageSize))
                .list();
    }


}
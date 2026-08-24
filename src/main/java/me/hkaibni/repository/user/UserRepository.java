package me.hkaibni.repository.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.UserSearchDTO;
import me.hkaibni.model.userdata.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findBySSN(String ssn) {
        return find("ssn", ssn).firstResult();
    }
    public User findById(String id) {
        return find("id", id).firstResult();
    }

    public void save(User user) {
        persist(user);
    }
    public List<User> listUsers(){
        return this.listAll();
    }
    public long deleteBySSN(String ssn) {
        return delete("ssn", ssn);
    }

    public List<User> search(String value, Integer page, Integer pageSize) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (value != null && !value.isBlank()) {
            String search = "%" + value.trim().toLowerCase() + "%";

            query.append("""
                and (
                    lower(firstName) like :search
                    or lower(lastName) like :search
                    or lower(email) like :search
                    or lower(ssn) like :search
                    or lower(phone) like :search
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

    public List<User> search(UserSearchDTO request) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (request.getSsn() != null && !request.getSsn().isBlank()) {
            query.append(" and lower(ssn) like :ssn");
            params.put(
                    "ssn",
                    "%" + request.getSsn().trim().toLowerCase() + "%"
            );
        }

        if (request.getEmail() != null &&
                !request.getEmail().isBlank()) {

            query.append(" and lower(email) = :email");
            params.put(
                    "email",
                    request.getEmail().trim().toLowerCase()
            );
        }

        if (request.getPhone() != null &&
                !request.getPhone().isBlank()) {

            query.append(" and phone like :phone");
            params.put(
                    "phone",
                    "%" + request.getPhone().trim() + "%"
            );
        }

        if (request.getAddressId() != null &&
                !request.getAddressId().isBlank()) {

            query.append(" and address.id = :addressId");
            params.put(
                    "addressId",
                    request.getAddressId().trim()
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
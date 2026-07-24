package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.User;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findBySSN(String SSN) {
        return find("SSN", SSN).firstResult();
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

    public List<User> globalSearch(String value) {
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


}
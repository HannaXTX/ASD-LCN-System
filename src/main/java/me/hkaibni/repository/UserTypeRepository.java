package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.roles.UserType;
@ApplicationScoped
public class UserTypeRepository implements PanacheRepository<UserType> {

    public UserType findByPrivilege(String privilege) {
        return find("privilege", privilege).firstResult();
    }

}

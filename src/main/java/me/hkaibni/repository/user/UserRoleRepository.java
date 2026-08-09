package me.hkaibni.repository.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.roles.UserRole;
@ApplicationScoped
public class UserRoleRepository implements PanacheRepository<UserRole> {

    public UserRole findByPrivilege(String privilege) {
        return find("privilege", privilege).firstResult();
    }

}

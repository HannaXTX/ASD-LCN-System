package me.hkaibni.model.roles_types;

import jakarta.persistence.*;

@Entity
@Table (name = "user_roles")
public class UserRole {

    @Id
    private int id;
    String privilege;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
}

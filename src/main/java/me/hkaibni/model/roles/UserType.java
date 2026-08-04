package me.hkaibni.model.roles;

import jakarta.persistence.*;

@Entity
public class UserType {

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

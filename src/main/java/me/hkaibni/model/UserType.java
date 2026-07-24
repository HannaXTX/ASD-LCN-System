package me.hkaibni.model;

import jakarta.persistence.*;

@Entity
public class UserType {


    public static enum privilegeType {ADMIN,BASIC}

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

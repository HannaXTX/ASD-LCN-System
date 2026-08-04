package me.hkaibni.dto.entity_dto;

import java.util.UUID;

public class FamilyMemberDTO {

    private UUID family;

    private UUID person;

    private boolean rootPerson;

    public UUID getFamily() {
        return family;
    }

    public void setFamily(UUID family) {
        this.family = family;
    }

    public UUID getPerson() {
        return person;
    }

    public void setPerson(UUID person) {
        this.person = person;
    }

    public boolean isRootPerson() {
        return rootPerson;
    }

    public void setRootPerson(boolean rootPerson) {
        this.rootPerson = rootPerson;
    }
}

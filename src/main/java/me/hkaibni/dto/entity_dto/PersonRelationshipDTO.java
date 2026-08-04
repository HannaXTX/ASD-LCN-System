package me.hkaibni.dto.entity_dto;

import me.hkaibni.model.roles.RelationshipType;

import java.util.UUID;

public class PersonRelationshipDTO {

    private UUID familyId;
    private UUID personAId;
    private UUID personBId;
    private RelationshipType type;

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }

    public UUID getPersonAId() {
        return personAId;
    }

    public void setPersonAId(UUID personAId) {
        this.personAId = personAId;
    }

    public UUID getPersonBId() {
        return personBId;
    }

    public void setPersonBId(UUID personBId) {
        this.personBId = personBId;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
}
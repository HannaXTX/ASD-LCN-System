package me.hkaibni.dto.request;

import me.hkaibni.model.roles_types.RelationshipType;

public class TreeRelationshipDTO {

    private String familyId;
    private String personAId;
    private String personBId;
    private RelationshipType type;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getPersonAId() {
        return personAId;
    }

    public void setPersonAId(String personAId) {
        this.personAId = personAId;
    }

    public String getPersonBId() {
        return personBId;
    }

    public void setPersonBId(String personBId) {
        this.personBId = personBId;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
}
package me.hkaibni.dto.response;

import me.hkaibni.model.family.Person;

import java.util.ArrayList;
import java.util.List;

public class FamilyNode {

    private Person person;
    private List<FamilyUnitNode> familyUnits = new ArrayList<>();

    public FamilyNode() {
    }


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<FamilyUnitNode> getFamilyUnits() {
        return familyUnits;
    }

    public void setFamilyUnits(List<FamilyUnitNode> familyUnits) {
        this.familyUnits = familyUnits;
    }
}
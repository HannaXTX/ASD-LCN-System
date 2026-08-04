package me.hkaibni.dto.response;

import me.hkaibni.model.family.Person;

import java.util.ArrayList;
import java.util.List;

public class FamilyUnitNode {


    private List<Person> spouses = new ArrayList<>();

    private List<FamilyNode> children = new ArrayList<>();

    public FamilyUnitNode() {
    }

    public List<Person> getSpouses() {
        return spouses;
    }

    public void setSpouses(List<Person> spouses) {
        this.spouses = spouses;
    }

    public List<FamilyNode> getChildren() {
        return children;
    }

    public void setChildren(List<FamilyNode> children) {
        this.children = children;
    }
}
package me.hkaibni.dto.response;

import me.hkaibni.model.family.Family;

public class FamilyTreeResponse {

    private Family family;
    private FamilyNode root;

    public FamilyTreeResponse() {
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public FamilyNode getRoot() {
        return root;
    }

    public void setRoot(FamilyNode root) {
        this.root = root;
    }
}
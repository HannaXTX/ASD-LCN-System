package me.hkaibni.dto.request;

public class FamilyMemberDTO {

    private String family;

    private String person;

    private boolean rootPerson;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public boolean isRootPerson() {
        return rootPerson;
    }

    public void setRootPerson(boolean rootPerson) {
        this.rootPerson = rootPerson;
    }
}

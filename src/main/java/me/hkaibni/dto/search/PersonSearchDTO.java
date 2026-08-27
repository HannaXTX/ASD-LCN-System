package me.hkaibni.dto.search;


import me.hkaibni.model.roles_types.Gender;

public class PersonSearchDTO extends SearchDTO{

    private String firstNameEn;
    private String firstNameAr;

    private String middleNameEn;
    private String middleNameAr;

    private String lastNameEn;
    private String lastNameAr;

    private String fullNameEn;
    private String fullNameAr;

    private Gender gender;

    public boolean hasNoCriteria() {
        return isBlank(firstNameEn)
                && isBlank(firstNameAr)
                && isBlank(middleNameEn)
                && isBlank(middleNameAr)
                && isBlank(lastNameEn)
                && isBlank(lastNameAr)
                && isBlank(fullNameEn)
                && isBlank(fullNameAr)
                && gender == null;
    }
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
    public String getFirstNameEn() {
        return firstNameEn;
    }

    public void setFirstNameEn(String firstNameEn) {
        this.firstNameEn = firstNameEn;
    }

    public String getFirstNameAr() {
        return firstNameAr;
    }

    public void setFirstNameAr(String firstNameAr) {
        this.firstNameAr = firstNameAr;
    }

    public String getMiddleNameEn() {
        return middleNameEn;
    }

    public void setMiddleNameEn(String middleNameEn) {
        this.middleNameEn = middleNameEn;
    }

    public String getMiddleNameAr() {
        return middleNameAr;
    }

    public void setMiddleNameAr(String middleNameAr) {
        this.middleNameAr = middleNameAr;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public String getLastNameAr() {
        return lastNameAr;
    }

    public void setLastNameAr(String lastNameAr) {
        this.lastNameAr = lastNameAr;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public String getFullNameAr() {
        return fullNameAr;
    }

    public void setFullNameAr(String fullNameAr) {
        this.fullNameAr = fullNameAr;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


}
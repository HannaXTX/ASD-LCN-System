package me.hkaibni.dto.request;



import me.hkaibni.model.roles_types.Gender;

import java.time.LocalDate;

public class UserUpdateDTO {

//        private Address address;
    private String firstNameEn;
    private String firstNameAr;

    private String middleNameAr;
    private String middleNameEn;

    private String lastNameAr;
    private String lastNameEn;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String phone;


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

    public String getMiddleNameAr() {
        return middleNameAr;
    }

    public void setMiddleNameAr(String middleNameAr) {
        this.middleNameAr = middleNameAr;
    }

    public String getMiddleNameEn() {
        return middleNameEn;
    }

    public void setMiddleNameEn(String middleNameEn) {
        this.middleNameEn = middleNameEn;
    }

    public String getLastNameAr() {
        return lastNameAr;
    }

    public void setLastNameAr(String lastNameAr) {
        this.lastNameAr = lastNameAr;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}




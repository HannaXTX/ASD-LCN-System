package me.hkaibni.dto.request;

import me.hkaibni.model.roles_types.Gender;

import java.time.LocalDate;

public class UserDTO {


    private String id;
    private String password;
    private String ssn;

    private String firstNameEn;
    private String firstNameAr;

    private String middleNameAr;
    private String middleNameEn;

    private String lastNameAr;
    private String lastNameEn;

    private String fullNameEn;
    private String fullNameAr;

    private LocalDate dateOfBirth;
    private String email;
    private String addressId;
    private String phone;
    private Gender gender;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSsn() {
        return ssn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setFirstNameEn(String firstNameEn) {
        this.firstNameEn = firstNameEn;
    }

    public void setFirstNameAr(String firstNameAr) {
        this.firstNameAr = firstNameAr;
    }

    public void setMiddleNameAr(String middleNameAr) {
        this.middleNameAr = middleNameAr;
    }

    public void setMiddleNameEn(String middleNameEn) {
        this.middleNameEn = middleNameEn;
    }

    public void setLastNameAr(String lastNameAr) {
        this.lastNameAr = lastNameAr;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public void setFullNameAr(String fullNameAr) {
        this.fullNameAr = fullNameAr;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstNameEn() {
        return firstNameEn;
    }

    public String getFirstNameAr() {
        return firstNameAr;
    }

    public String getMiddleNameAr() {
        return middleNameAr;
    }

    public String getMiddleNameEn() {
        return middleNameEn;
    }

    public String getLastNameAr() {
        return lastNameAr;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public String getFullNameAr() {
        return fullNameAr;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }
}
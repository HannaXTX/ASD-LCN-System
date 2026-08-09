package me.hkaibni.dto.entity_dto;

import me.hkaibni.model.roles.Gender;

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
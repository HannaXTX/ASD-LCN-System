package me.hkaibni.model;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


@Entity
@Table (name = "USERS")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String SSN;
    @OneToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;
    private Date dateOfBirth;
    private String firstName;
    private String lastName;
    private int approved;
    private int verified;

    private String password;
    private String email;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private String phone;
    private Date created;
    private Date modified;
    private Date lastLogged;
    private byte[] salt;
    private boolean isActive;

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Column(columnDefinition = "TEXT")
    private String token;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(Date lastLogged) {
        this.lastLogged = lastLogged;
    }


    public void setSalt(byte[] salt) {
        this.salt = salt;
    }


    public byte[] getSalt() {
        return salt;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "User{\n" +
                "  id=" + id + ",\n" +
                "  SSN='" + SSN + "',\n" +
                "  userType=" + userType + ",\n" +
                "  dateOfBirth=" + dateOfBirth + ",\n" +
                "  firstName='" + firstName + "',\n" +
                "  lastName='" + lastName + "',\n" +
                "  approved=" + approved + ",\n" +
                "  verified=" + verified + ",\n" +
                "  password='" + password + "',\n" +
                "  email='" + email + "',\n" +
                "  address=" + address + ",\n" +
                "  phone='" + phone + "',\n" +
                "  created=" + created + ",\n" +
                "  modified=" + modified + ",\n" +
                "  lastLogged=" + lastLogged + ",\n" +
                "  salt=" + Arrays.toString(salt) + ",\n" +
                "  isActive=" + isActive + ",\n" +
                "  token='" + token + "'\n" +
                "}";
    }
}

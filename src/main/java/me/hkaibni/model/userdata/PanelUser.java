package me.hkaibni.model.userdata;
import jakarta.persistence.*;
import me.hkaibni.model.Address;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table (name = "panel_users")
public class PanelUser {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String ssn;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            optional = false
    )
    @JoinColumn(
            name = "account_id",
            nullable = false,
            unique = true
    )
    private PanelUserAccount userAccount;

    private LocalDate dateOfBirth;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // AUDIT DATA
    private LocalDateTime lastLoggedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String createdBy;



    public PanelUserAccount getAccount(){
        return userAccount;
    }

    public void setAccount(PanelUserAccount userAccount) {
        this.userAccount = userAccount;

    }
;
    public PanelUser() {

    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String SSN) {
        this.ssn = SSN;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
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





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




}

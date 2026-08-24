package me.hkaibni.model.userdata;
import jakarta.persistence.*;
import me.hkaibni.model.Address;
import me.hkaibni.model.family.Person;

import java.time.LocalDateTime;


@Entity
@Table (name = "users")
public class User {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String ssn;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "person_id",
            nullable = false,
            unique = true
    )
    private Person person;


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
    private UserAccount userAccount;

    private String email;
    private String phone;


    // AUDIT DATA
    private LocalDateTime lastLoggedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public LocalDateTime getLastLoggedAt() {
        return lastLoggedAt;
    }

    public void setLastLoggedAt(LocalDateTime lastLoggedAt) {
        this.lastLoggedAt = lastLoggedAt;
    }



    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public UserAccount getAccount(){
        return userAccount;
    }

    public void setAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    public User() {

    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
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


    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

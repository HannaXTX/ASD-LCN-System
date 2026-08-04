package me.hkaibni.model.userdata;
import jakarta.persistence.*;
import me.hkaibni.model.Address;
import me.hkaibni.model.family.Person;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table (name = "USERS")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

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
    private Account account;

//    private LocalDate dateOfBirth;
//    private String firstName;
//    private String lastName;

    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Account getAccount(){
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public User() {

    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String SSN) {
        this.ssn = SSN;
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





    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }




}

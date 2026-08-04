package me.hkaibni.model.family;

import jakarta.persistence.*;
import me.hkaibni.model.roles.Gender;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Gender getGender() {

        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
package me.hkaibni.model.family;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "family_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_family_member",
                        columnNames = {"family_id", "person_id"}
                )
        }
)
public class FamilyMember {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "is_root_person", nullable = false)
    private boolean rootPerson;

    public FamilyMember() {
    }

    public UUID getId() {
        return id;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isRootPerson() {
        return rootPerson;
    }

    public void setRootPerson(boolean rootPerson) {
        this.rootPerson = rootPerson;
    }
}
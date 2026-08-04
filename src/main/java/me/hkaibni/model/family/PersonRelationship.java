package me.hkaibni.model.family;

import jakarta.persistence.*;
import me.hkaibni.model.roles.RelationshipType;

import java.util.UUID;

@Entity
@Table(
        name = "person_relationships",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_person_relationship",
                        columnNames = {
                                "family_id",
                                "person_a_id",
                                "person_b_id",
                                "relationship_type"
                        }
                )
        }
)
public class PersonRelationship {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_a_id", nullable = false)
    private Person personA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_b_id", nullable = false)
    private Person personB;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "relationship_type",
            nullable = false,
            length = 40
    )
    private RelationshipType type;

    public UUID getId() {
        return id;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Person getPersonA() {
        return personA;
    }

    public void setPersonA(Person personA) {
        this.personA = personA;
    }

    public Person getPersonB() {
        return personB;
    }

    public void setPersonB(Person personB) {
        this.personB = personB;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
}
package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hkaibni.model.family.FamilyMember;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.family.PersonRelationship;
import me.hkaibni.model.roles.Gender;
import me.hkaibni.model.roles.RelationshipType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonRelationshipRepository
        implements PanacheRepositoryBase<PersonRelationship, UUID> {


    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Inject
    public PersonRelationshipRepository(FamilyRepository familyRepository, FamilyMemberRepository familyMemberRepository) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
    }

    public List<PersonRelationship> ListPersonRelationshipsByFamily(UUID id){
        return this.list("family",id);
    }

    public List<PersonRelationship> ListPersonRelationships(){
        return this.listAll();
    }
    public boolean existsDirected(
            UUID familyId,
            UUID personAId,
            UUID personBId,
            RelationshipType type
    ) {
        return count(
                "family.id = ?1 " +
                        "and personA.id = ?2 " +
                        "and personB.id = ?3 " +
                        "and type = ?4",
                familyId,
                personAId,
                personBId,
                type
        ) > 0;
    }

    public boolean existsInEitherDirection(
            UUID familyId,
            UUID personAId,
            UUID personBId,
            RelationshipType type
    ) {
        return count(
                "family.id = ?1 " +
                        "and type = ?4 " +
                        "and (" +
                        "(personA.id = ?2 and personB.id = ?3) " +
                        "or " +
                        "(personA.id = ?3 and personB.id = ?2)" +
                        ")",
                familyId,
                personAId,
                personBId,
                type
        ) > 0;
    }

    public void save(PersonRelationship relationship) {
        persist(relationship);
    }

    public List<FamilyMember> getSpousesFemaleById(UUID id) {

        List<FamilyMember> wives = new ArrayList<>();

        List<PersonRelationship> spousesBothGender = list(
                "type = ?1 and (personA.id = ?2 or personB.id = ?2)",
                RelationshipType.SPOUSE_OF,
                id
        );
        for (PersonRelationship spouseRelationship : spousesBothGender) {

            Person spouse;

            if (spouseRelationship.getPersonA().getId().equals(id)) {
                spouse = spouseRelationship.getPersonB();
            } else {
                spouse = spouseRelationship.getPersonA();
            }

            if (spouse.getGender() == Gender.FEMALE) {


                FamilyMember spouseMember =
                        familyMemberRepository.findByPersonId(spouse.getId());

                if (spouseMember != null) {
                    wives.add(spouseMember);
                }
            }
        }

        return wives;
    }

    public List<FamilyMember> getChildrenById(UUID id) {

        List<FamilyMember> children = new ArrayList<>();

        List<PersonRelationship> childrenRelations = list(
                "type = ?1 and personA.id = ?2",
                RelationshipType.PARENT_OF,
                id
        );

        for (PersonRelationship relationship : childrenRelations) {

            Person childPerson = relationship.getPersonB();

            FamilyMember childMember = familyMemberRepository.findByPersonId(childPerson.getId());

            if (childMember != null) {
                children.add(childMember);
            }
        }

        return children;
    }


}



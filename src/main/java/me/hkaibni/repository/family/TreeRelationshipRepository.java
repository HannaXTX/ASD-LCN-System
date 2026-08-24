package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hkaibni.model.family.FamilyMember;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.family.TreeRelationship;
import me.hkaibni.model.roles_types.Gender;
import me.hkaibni.model.roles_types.RelationshipType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TreeRelationshipRepository
        implements PanacheRepositoryBase<TreeRelationship, UUID> {


    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Inject
    public TreeRelationshipRepository(FamilyRepository familyRepository, FamilyMemberRepository familyMemberRepository) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
    }



    public List<TreeRelationship> ListTreeRelationshipsByFamily(UUID id){
        return this.list("family",id);
    }

    public List<TreeRelationship> listTreeRelationships(){
        return this.listAll();
    }
    public boolean existsDirected(
            String familyId,
            String personAId,
            String personBId,
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
            String familyId,
            String personAId,
            String personBId,
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

    public void save(TreeRelationship relationship) {
        persist(relationship);
    }

    public List<FamilyMember> getSpousesFemaleById(String id) {

        List<FamilyMember> wives = new ArrayList<>();

        List<TreeRelationship> spousesBothGender = list(
                "type = ?1 and (personA.id = ?2 or personB.id = ?2)",
                RelationshipType.SPOUSE_OF,
                id
        );
        for (TreeRelationship spouseRelationship : spousesBothGender) {

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

    public List<FamilyMember> getSpousesMaleById(String id) {

        List<FamilyMember> wives = new ArrayList<>();

        List<TreeRelationship> spousesBothGender = list(
                "type = ?1 and (personA.id = ?2 or personB.id = ?2)",
                RelationshipType.SPOUSE_OF,
                id
        );
        for (TreeRelationship spouseRelationship : spousesBothGender) {

            Person spouse;

            if (spouseRelationship.getPersonA().getId().equals(id)) {
                spouse = spouseRelationship.getPersonB();
            } else {
                spouse = spouseRelationship.getPersonA();
            }

            if (spouse.getGender() == Gender.MALE) {


                FamilyMember spouseMember =
                        familyMemberRepository.findByPersonId(spouse.getId());

                if (spouseMember != null) {
                    wives.add(spouseMember);
                }
            }
        }

        return wives;
    }

    public List<FamilyMember> getChildrenById(String id) {

        List<FamilyMember> children = new ArrayList<>();

        List<TreeRelationship> childrenRelations = list(
                "type = ?1 and personA.id = ?2",
                RelationshipType.PARENT_OF,
                id
        );

        for (TreeRelationship relationship : childrenRelations) {

            Person childPerson = relationship.getPersonB();

            FamilyMember childMember = familyMemberRepository.findByPersonId(childPerson.getId());

            if (childMember != null) {
                children.add(childMember);
            }
        }

        return children;
    }


}



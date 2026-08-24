package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.request.TreeRelationshipDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.family.TreeRelationship;
import me.hkaibni.model.roles_types.RelationshipType;
import me.hkaibni.repository.family.FamilyMemberRepository;
import me.hkaibni.repository.family.FamilyRepository;
import me.hkaibni.repository.family.PersonRepository;
import me.hkaibni.repository.family.TreeRelationshipRepository;

import java.util.UUID;

@ApplicationScoped
public class TreeRelationshipService {

    @Inject
    FamilyRepository familyRepository;

    @Inject
    PersonRepository personRepository;

    @Inject
    FamilyMemberRepository familyMemberRepository;

    @Inject
    TreeRelationshipRepository relationshipRepository;


    public Family getFamilyById(String id) {
        return familyRepository.findById(id);
    }


    public Person getPersonById(String id) {
        return personRepository.findById(id);
    }


    public boolean isMember(String familyId, String personId) {
        return familyMemberRepository.existsByFamilyAndPerson(
                familyId,
                personId
        );
    }


    public boolean relationshipExists(TreeRelationshipDTO dto) {

        if (isSymmetric(dto.getType())) {
            return relationshipRepository.existsInEitherDirection(
                    dto.getFamilyId(),
                    dto.getPersonAId(),
                    dto.getPersonBId(),
                    dto.getType()
            );
        }

        return relationshipRepository.existsDirected(
                dto.getFamilyId(),
                dto.getPersonAId(),
                dto.getPersonBId(),
                dto.getType()
        );
    }


    @Transactional
    public TreeRelationship createRelationship(TreeRelationshipDTO dto) {

        Family family = familyRepository.findById(dto.getFamilyId());
        Person personA = personRepository.findById(dto.getPersonAId());
        Person personB = personRepository.findById(dto.getPersonBId());

        TreeRelationship relationship = new TreeRelationship();

        relationship.setId(UUID.randomUUID().toString());
        relationship.setFamily(family);
        relationship.setPersonA(personA);
        relationship.setPersonB(personB);
        relationship.setType(dto.getType());

        relationshipRepository.save(relationship);

        return relationship;
    }


    private boolean isSymmetric(RelationshipType type) {
        return type == RelationshipType.SPOUSE_OF;
    }
}
package me.hkaibni.service.family;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.entity_dto.TreeRelationshipDTO;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.family.Person;
import me.hkaibni.model.family.TreeRelationship;
import me.hkaibni.model.roles.RelationshipType;
import me.hkaibni.repository.family.FamilyMemberRepository;
import me.hkaibni.repository.family.FamilyRepository;
import me.hkaibni.repository.family.TreeRelationshipRepository;
import me.hkaibni.repository.family.PersonRepository;
import me.hkaibni.service.status.RelationshipCreationStatus;

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

    @Transactional
    public RelationshipCreationStatus createRelationship(
            TreeRelationshipDTO dto
    ) {

        if (dto == null ||
                dto.getFamilyId() == null ||
                dto.getPersonAId() == null ||
                dto.getPersonBId() == null ||
                dto.getType() == null) {

            return RelationshipCreationStatus.INVALID_REQUEST;
        }

        if (dto.getPersonAId().equals(dto.getPersonBId())) {
            return RelationshipCreationStatus.SELF_RELATIONSHIP;
        }

        Family family = familyRepository.findById(dto.getFamilyId());

        if (family == null) {
            return RelationshipCreationStatus.FAMILY_NOT_FOUND;
        }

        Person personA = personRepository.findById(dto.getPersonAId());

        if (personA == null) {
            return RelationshipCreationStatus.PERSON_A_NOT_FOUND;
        }

        Person personB = personRepository.findById(dto.getPersonBId());

        if (personB == null) {
            return RelationshipCreationStatus.PERSON_B_NOT_FOUND;
        }

        boolean personAIsMember =
                familyMemberRepository.existsByFamilyAndPerson(
                        family.getId(),
                        personA.getId()
                );

        if (!personAIsMember) {
            return RelationshipCreationStatus.PERSON_A_NOT_IN_FAMILY;
        }

        boolean personBIsMember =
                familyMemberRepository.existsByFamilyAndPerson(
                        family.getId(),
                        personB.getId()
                );

        if (!personBIsMember) {
            return RelationshipCreationStatus.PERSON_B_NOT_IN_FAMILY;
        }

        boolean alreadyExists;

        if (isSymmetric(dto.getType())) {
            alreadyExists =
                    relationshipRepository.existsInEitherDirection(
                            family.getId(),
                            personA.getId(),
                            personB.getId(),
                            dto.getType()
                    );
        } else {
            alreadyExists =
                    relationshipRepository.existsDirected(
                            family.getId(),
                            personA.getId(),
                            personB.getId(),
                            dto.getType()
                    );
        }

        if (alreadyExists) {
            return RelationshipCreationStatus.ALREADY_EXISTS;
        }

        TreeRelationship relationship =
                new TreeRelationship();

        relationship.setId(UUID.randomUUID().toString());
        relationship.setFamily(family);
        relationship.setPersonA(personA);
        relationship.setPersonB(personB);
        relationship.setType(dto.getType());

        relationshipRepository.save(relationship);

        return RelationshipCreationStatus.SUCCESS;
    }

    private boolean isSymmetric(RelationshipType type) {
        return switch (type) {
            case SPOUSE_OF -> true;
            default -> false;
        };
    }



}

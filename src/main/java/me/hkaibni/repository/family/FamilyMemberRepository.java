package me.hkaibni.repository.family;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import me.hkaibni.controller.family_related.FamilyMemberController;
import me.hkaibni.dto.response.FamilyNode;
import me.hkaibni.dto.response.FamilyTreeResponse;
import me.hkaibni.dto.response.FamilyUnitNode;
import me.hkaibni.model.family.Family;
import me.hkaibni.model.family.FamilyMember;
import me.hkaibni.model.family.Person;
import me.hkaibni.service.family.FamilyMemberService;

import java.util.*;

@ApplicationScoped
public class FamilyMemberRepository implements PanacheRepository<FamilyMember> {

    @Inject
    TreeRelationshipRepository personRelationshipRepository;
    @Inject
    FamilyRepository familyRepository;
    @Inject
    FamilyMemberController familyMemberController;
    @Inject
    FamilyMemberService familyMemberService;
    @Inject
    PersonRepository personRepository;
    @Inject
    FamilyMemberRepository familyMemberRepository;


    public FamilyMember findById(UUID id) {
        return find("id", id).firstResult();
    }
    public FamilyMember findByPersonId(String id){
        return find("person.id",id).firstResult();
    }
    public FamilyMember findByFamilyId(UUID id){
        return find("family",id).firstResult();
    }

    public FamilyMember findByPersonAndFamily(
            Person person,
            Family family
    ) {
        return find(
                "person = ?1 and family = ?2",
                person,
                family
        ).firstResult();
    }

    public void save(FamilyMember family) {
        persist(family);
    }

    public List<FamilyMember> listFamilyMembers() {
        return listAll();
    }


    public long deleteFamilyMembersById(UUID id) {
        return delete("id", id);
    }

    public boolean existsByFamilyAndPerson(
            String familyId,
            String personId
    ) {
        return count(
                "family.id = ?1 and person.id = ?2",
                familyId,
                personId
        ) > 0;
    }

    public FamilyMember getRootPersonByFamily(UUID id){
        return find("rootPerson = ?1 and family.id=?2",true,id).firstResult();
    }

//    public FamilyMember getSpousesByPerson(UUID id){
//        return ;
//    }

    public List<FamilyMember> getSpouses(String id){
        return personRelationshipRepository.getSpousesFemaleById(id);
    }
    public List<FamilyMember> getChildren(String id){
        return personRelationshipRepository.getChildrenById(id);
    }

    public FamilyTreeResponse buildTree(UUID familyId) {

        FamilyMember rootPerson = getRootPersonByFamily(familyId);

        if (rootPerson == null) {
            throw new NotFoundException();
        }

        FamilyTreeResponse familyTreeResponse = new FamilyTreeResponse();
        familyTreeResponse.setFamily(rootPerson.getFamily());

        FamilyNode rootNode = buildFamilyNode(
                rootPerson,
                new HashSet<>()
        );

        familyTreeResponse.setRoot(rootNode);

        return familyTreeResponse;
    }


    private FamilyNode buildFamilyNode(
            FamilyMember currentMember,
            Set<String> visited
    ) {

        Person currentPerson = currentMember.getPerson();

        FamilyNode currentNode = new FamilyNode();
        currentNode.setPerson(currentPerson);


        if (!visited.add(currentPerson.getId())) {
            currentNode.setFamilyUnits(new ArrayList<>());
            return currentNode;
        }

        List<FamilyMember> spouseMembers =
                personRelationshipRepository.getSpousesFemaleById(currentPerson.getId());

        List<FamilyMember> childMembers =
                personRelationshipRepository.getChildrenById(currentPerson.getId());


        if (spouseMembers.isEmpty() && childMembers.isEmpty()) {
            currentNode.setFamilyUnits(new ArrayList<>());
            return currentNode;
        }

        FamilyUnitNode familyUnitNode = new FamilyUnitNode();

        List<Person> spouses = new ArrayList<>();

        for (FamilyMember spouseMember : spouseMembers) {
            spouses.add(spouseMember.getPerson());
        }

        familyUnitNode.setSpouses(spouses);

        List<FamilyNode> children = new ArrayList<>();

        for (FamilyMember childMember : childMembers) {

            FamilyNode childNode = buildFamilyNode(
                    childMember,
                    new HashSet<>(visited)
            );

            children.add(childNode);
        }

        familyUnitNode.setChildren(children);

        List<FamilyUnitNode> familyUnits = new ArrayList<>();
        familyUnits.add(familyUnitNode);

        currentNode.setFamilyUnits(familyUnits);

        return currentNode;
    }
}

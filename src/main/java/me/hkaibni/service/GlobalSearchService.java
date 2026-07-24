package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hkaibni.dto.SearchResultDTO;
import me.hkaibni.dto.UserDTO;
import me.hkaibni.model.Address;
import me.hkaibni.model.Family;
import me.hkaibni.model.User;
import me.hkaibni.repository.AddressRepository;
import me.hkaibni.repository.FamilyRepository;
import me.hkaibni.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GlobalSearchService {

    @Inject
    UserRepository userRepository;

    @Inject
    FamilyRepository familyRepository;

    @Inject
    AddressRepository addressRepository;

    public List<SearchResultDTO> search(String value) {
        List<SearchResultDTO> results = new ArrayList<>();

        List<User> users = userRepository.globalSearch(value);

        for (User user : users) {
            UserDTO dto = new UserDTO();

            dto.setId(user.getId());
            dto.setSSN(user.getSSN());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setAddressId(
                    user.getAddress() != null
                            ? user.getAddress().getId()
                            : null
            );
            dto.setPhone(user.getPhone());
            results.add(new SearchResultDTO(
                    "USER",
                    user.getId(),
                    dto
            ));
        }

        List<Family> families = familyRepository.globalSearch(value);

        for (Family family : families) {
            results.add(new SearchResultDTO(
                    "FAMILY",
                    family.getId(),
                    family.getFamilyName()
            ));
        }

        List<Address> addresses = addressRepository.globalSearch(value);

        for (Address address : addresses) {
            results.add(new SearchResultDTO(
                    "ADDRESS",
                    address.getId(),
                    address.getGovernorate() + " - " + address.getVillage()
            ));
        }

        return results;
    }
}
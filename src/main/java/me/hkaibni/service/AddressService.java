package me.hkaibni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.hkaibni.dto.search.AddressSearchDTO;
import me.hkaibni.dto.entity_dto.AddressDTO;
import me.hkaibni.model.Address;
import me.hkaibni.repository.AddressRepository;
import me.hkaibni.repository.UserRepository;

import java.util.List;

@ApplicationScoped
public class AddressService {

    @Inject
    UserRepository userRepository;
    @Inject
    AddressRepository addressRepository;



    @Transactional
    public boolean createAddress(AddressDTO dto) throws Exception {

        if (addressRepository.findByCode(dto.getCode()) != null) {
            return false;
        }


        Address address = new Address();

        address.setCode(dto.getCode());
        address.setGovernorate(dto.getGovernorate());
        address.setVillage(dto.getVillage());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());

        addressRepository.save(address);

        return true;
    }
    @Transactional
    public Address getAddress(String address) {
        return addressRepository.findById(address);
    }
    @Transactional
    public List<Address> getAllAddresses(){
        return addressRepository.listAddresses();
    }

    @Transactional
    public int updateAddress(String id, AddressDTO dto) throws Exception {

        Address address = addressRepository.findById(id);

        if (address == null) {
            return 1;
        }
        if (addressRepository.findById(id) != null && !id.equals(dto.getCode())) {
            return 2;
        }

        address.setCode(dto.getCode());
        address.setGovernorate(dto.getGovernorate());
        address.setVillage(dto.getVillage());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());

        return 0;
    }

    public List<Address> searchAddresses(AddressSearchDTO request) {
        return addressRepository.search(request);
    }

    public List<Address> searchAddresses(String request) {
        return addressRepository.search(request);
    }

    @Transactional
    public boolean deleteAddress(String id) {
        return addressRepository.deleteById(id) > 0;
    }
}
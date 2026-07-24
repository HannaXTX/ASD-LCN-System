package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.Address;

import java.util.List;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {

    public Address findById(String id) {
        return find("id", id).firstResult();
    }

    public void save(Address address) {
        persist(address);
    }
    public List<Address> listAddresses(){
        return this.listAll();
    }
    public long deleteById(String id) {
        return delete("id", id);
    }

    public List<Address> globalSearch(String value) {
        String search = "%" + value.toLowerCase() + "%";

        return list(
                "LOWER(id) LIKE ?1 " +
                        "OR LOWER(governorate) LIKE ?1 " +
                        "OR LOWER(village) LIKE ?1",
                search
        );
    }

}

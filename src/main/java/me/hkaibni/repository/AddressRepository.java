package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.AddressSearchDTO;
import me.hkaibni.model.Address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {

    public Address findById(String id) {
        return find("id", id).firstResult();
    }

    public Address findByCode(String code) {
        return find("code", code).firstResult();
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

    public List<Address> search(String value) {
        String search = "%" + value.toLowerCase() + "%";

        return list(
                "LOWER(id) LIKE ?1 " + "LOWER(code) LIKE ?1 " +
                "OR LOWER(governorate) LIKE ?1 " +
                        "OR LOWER(village) LIKE ?1",
                search
        );
    }


    public List<Address> search(AddressSearchDTO request) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (request.getCode() != null && !request.getCode().isBlank()) {
            query.append(" and LOWER(code) like :code");
            params.put(
                    "code",
                    "%" + request.getGovernorate().trim().toLowerCase() + "%"
            );
        }

        if (request.getGovernorate() != null && !request.getGovernorate().isBlank()) {
            query.append(" and LOWER(governorate) like :governorate");
            params.put(
                    "governorate",
                    "%" + request.getGovernorate().trim().toLowerCase() + "%"
            );
        }

        if (request.getVillage() != null && !request.getVillage().isBlank()) {

            query.append(" and LOWER(village) like :village");
            params.put(
                    "village",
                    "%" + request.getVillage().trim().toLowerCase() + "%"
            );
        }

        int page = request.getPage() == null
                ? 0
                : Math.max(request.getPage(), 0);

        int pageSize = request.getPageSize() == null
                ? 20
                : Math.min(Math.max(request.getPageSize(), 1), 100);

        return find(query.toString(), params)
                .page(Page.of(page, pageSize))
                .list();
    }

}

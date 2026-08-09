package me.hkaibni.repository.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.userdata.PanelUserAccount;

import java.util.List;
import java.util.UUID;
@ApplicationScoped
public class PanelUserAccountRepository implements PanacheRepository<PanelUserAccount> {


    public PanelUserAccount findById(UUID id){
        return find("id",id).firstResult();
    }
    public void save(PanelUserAccount userAccount) {
        persist(userAccount);
    }
    public List<PanelUserAccount> listAccounts(){
        return this.listAll();
    }
    public long deleteById(UUID id) {
        return delete("id", id);
    }


}

package me.hkaibni.repository.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.userdata.UserAccount;

import java.util.List;
import java.util.UUID;
@ApplicationScoped
public class UserAccountRepository implements PanacheRepository<UserAccount> {

    public UserAccount findById(UUID id){
        return find("id",id).firstResult();
    }
    public void save(UserAccount userAccount) {
        persist(userAccount);
    }
    public List<UserAccount> listAccounts(){
        return this.listAll();
    }
    public long deleteById(UUID id) {
        return delete("id", id);
    }


}

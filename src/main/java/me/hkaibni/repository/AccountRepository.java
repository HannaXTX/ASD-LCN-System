package me.hkaibni.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.Account;
import me.hkaibni.model.Account;

import java.util.List;
import java.util.UUID;
@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public Account findById(UUID id){
        return find("id",id).firstResult();
    }
    public void save(Account account) {
        persist(account);
    }
    public List<Account> ListAccounts(){
        return this.listAll();
    }
    public long deleteById(UUID id) {
        return delete("id", id);
    }


}

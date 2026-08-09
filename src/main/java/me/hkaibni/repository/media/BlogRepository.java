package me.hkaibni.repository.media;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.media.Blog;

import java.util.List;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    public Blog findById(String id) {
        return find("id", id).firstResult();
    }

    public void save(Blog blog) { persist(blog);}
    public List<Blog> listBlogs(){
        return this.listAll();
    }
    public long deleteById(String id) {
        return delete("id", id);
    }


}

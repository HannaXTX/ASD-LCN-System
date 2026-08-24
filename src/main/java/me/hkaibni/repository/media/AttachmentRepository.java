package me.hkaibni.repository.media;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.model.media.Blog;

import java.util.List;

@ApplicationScoped
public class AttachmentRepository implements PanacheRepository<Attachment> {

    public void save(Attachment attachment) {
        persist(attachment);
    }

    public Attachment findById(String id) {
        return find("id",id).firstResult();

    }

    public List<Attachment> listBlogs(){
        return this.listAll();
    }
    public long deleteById(String id) {
        return delete("id", id);
    }

}

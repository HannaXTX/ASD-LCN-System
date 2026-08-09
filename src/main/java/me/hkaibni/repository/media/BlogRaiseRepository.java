package me.hkaibni.repository.media;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.media.BlogRaise;

import java.util.List;

@ApplicationScoped
public class BlogRaiseRepository
        implements PanacheRepositoryBase<BlogRaise, String> {


    public BlogRaise findById(String id) {
        return find("id", id).firstResult();
    }


    public void save(BlogRaise blogRaise) {
        persist(blogRaise);
    }


    public List<BlogRaise> listBlogRaises() {
        return listAll();
    }


    public BlogRaise findByBlogAndUser(
            String blogId,
            String raisedBy
    ) {
        return find(
                "blog.id = ?1 and raisedBy = ?2",
                blogId,
                raisedBy
        ).firstResult();
    }


    public boolean hasRaised(
            String blogId,
            String raisedBy
    ) {
        return count(
                "blog.id = ?1 and raisedBy = ?2",
                blogId,
                raisedBy
        ) > 0;
    }


    public long countByBlog(String blogId) {
        return count("blog.id", blogId);
    }


    public long deleteByBlogAndUser(
            String blogId,
            String raisedBy
    ) {
        return delete(
                "blog.id = ?1 and raisedBy = ?2",
                blogId,
                raisedBy
        );
    }
}
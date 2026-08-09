package me.hkaibni.repository.media;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.model.media.News;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NewsRepository implements PanacheRepository<News> {

    public News findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void save(News news) {
        persist(news);
    }

    public List<News> listNews() {
        return listAll();
    }

    public long deleteById(UUID id) {
        return delete("id", id);
    }
}
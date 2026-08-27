package me.hkaibni.repository.media;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.NewsSearchDTO;
import me.hkaibni.model.media.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class NewsRepository implements PanacheRepository<News> {

    public News findById(String id) {
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

    public List<News> search(
            String value,
            Integer page,
            Integer pageSize
    ) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (value != null && !value.isBlank()) {

            String search = "%" + value.trim().toLowerCase() + "%";

            query.append("""
                and (
                    lower(title) like :search
                    or lower(content) like :search
                    or lower(createdBy) like :search
                )
                """);

            params.put("search", search);
        }

        var panacheQuery = find(query.toString(), params);

        if (page != null && page == -1) {
            return panacheQuery.list();
        }

        int resolvedPage = page == null
                ? 1
                : Math.max(page, 1);

        int resolvedPageSize = pageSize == null
                ? 20
                : Math.clamp(pageSize, 1, 100);

        return panacheQuery
                .page(Page.of(resolvedPage - 1, resolvedPageSize))
                .list();
    }




    public List<News> search(NewsSearchDTO request) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!isBlank(request.getTitle())) {
            query.append(" and lower(title) like :title");

            params.put(
                    "title",
                    "%" + request.getTitle().trim().toLowerCase() + "%"
            );
        }

        if (!isBlank(request.getContent())) {
            query.append(" and lower(content) like :content");

            params.put(
                    "content",
                    "%" + request.getContent().trim().toLowerCase() + "%"
            );
        }

        if (!isBlank(request.getCreatedBy())) {
            query.append(" and lower(createdBy) like :createdBy");

            params.put(
                    "createdBy",
                    "%" + request.getCreatedBy().trim().toLowerCase() + "%"
            );
        }

        if (request.getPublishedAfter() != null) {
            query.append(" and publishedAt >= :publishedAfter");
            params.put("publishedAfter", request.getPublishedAfter());
        }

        if (request.getPublishedBefore() != null) {
            query.append(" and publishedAt <= :publishedBefore");
            params.put("publishedBefore", request.getPublishedBefore());
        }

        if (request.getCreatedAfter() != null) {
            query.append(" and createdAt >= :createdAfter");
            params.put("createdAfter", request.getCreatedAfter());
        }

        if (request.getCreatedBefore() != null) {
            query.append(" and createdAt <= :createdBefore");
            params.put("createdBefore", request.getCreatedBefore());
        }

        var panacheQuery = find(query.toString(), params);

        if (request.getPage() != null && request.getPage() == -1) {
            return panacheQuery.list();
        }

        int page = request.getPage() == null
                ? 1
                : Math.max(request.getPage(), 1);

        int pageSize = request.getPageSize() == null
                ? 20
                : Math.clamp(request.getPageSize(), 1, 100);

        return panacheQuery
                .page(Page.of(page - 1, pageSize))
                .list();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
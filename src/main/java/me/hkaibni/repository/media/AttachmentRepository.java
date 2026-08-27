package me.hkaibni.repository.media;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import me.hkaibni.dto.search.AttachmentSearchDTO;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.model.media.Blog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AttachmentRepository implements PanacheRepository<Attachment> {

    public void save(Attachment attachment) {
        persist(attachment);
    }

    public Attachment findById(String id) {
        return find("id",id).firstResult();

    }

    public List<Attachment> listAttachments(){
        return this.listAll();
    }
    public long deleteById(String id) {
        return delete("id", id);
    }


    public List<Attachment> search(String value, Integer page, Integer pageSize) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (value != null && !value.isBlank()) {

            String search = "%" + value.trim().toLowerCase() + "%";

            query.append("""
                and (
                    lower(originalName) like :search
                    or lower(contentType) like :search
                    or lower(uploadedBy) like :search
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
    public List<Attachment> search(AttachmentSearchDTO request) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (request.getOriginalName() != null &&
                !request.getOriginalName().isBlank()) {

            query.append(" and lower(originalName) like :originalName");

            params.put(
                    "originalName",
                    "%" + request.getOriginalName().trim().toLowerCase() + "%"
            );
        }

        if (request.getContentType() != null &&
                !request.getContentType().isBlank()) {

            query.append(" and lower(contentType) like :contentType");

            params.put(
                    "contentType",
                    "%" + request.getContentType().trim().toLowerCase() + "%"
            );
        }

        if (request.getUploadedBy() != null &&
                !request.getUploadedBy().isBlank()) {

            query.append(" and uploadedBy = :uploadedBy");

            params.put(
                    "uploadedBy",
                    request.getUploadedBy().trim()
            );
        }

        if (request.getMinSizeBytes() != null) {
            query.append(" and sizeBytes >= :minSizeBytes");
            params.put("minSizeBytes", request.getMinSizeBytes());
        }

        if (request.getMaxSizeBytes() != null) {
            query.append(" and sizeBytes <= :maxSizeBytes");
            params.put("maxSizeBytes", request.getMaxSizeBytes());
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

}

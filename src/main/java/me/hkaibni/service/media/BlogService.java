package me.hkaibni.service.media;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import me.hkaibni.dto.request.BlogCreationDTO;
import me.hkaibni.dto.search.AttachmentSearchDTO;
import me.hkaibni.dto.search.BlogSearchDTO;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.model.media.Blog;
import me.hkaibni.repository.media.AttachmentRepository;
import me.hkaibni.repository.media.BlogRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    JsonWebToken jwt;
    @Inject
    AttachmentRepository attachmentRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.listBlogs();
    }
    @Transactional
    public Blog getBlog(String blog) {
        return blogRepository.findById(blog);
    }

    @Transactional
    public Blog createBlog(BlogCreationDTO dto) {

        String creatorId = jwt.getSubject();

        Blog blog = new Blog();


        blog.setId(UUID.randomUUID().toString());
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setPublishedAt(LocalDateTime.now());
        blog.setCreatedBy(creatorId);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setModifiedAt(LocalDateTime.now());

        blog.setAttachments(
                getAttachments(dto.getAttachmentIds())
        );

        blogRepository.save(blog);
        return blog;
    }

    @Transactional
    public Blog updateBlog(String id, BlogCreationDTO dto) {

        Blog blog = blogRepository.findById(id);

        if (blog == null)
            return null;

        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());

        if (dto.getAttachmentIds() != null) {
            blog.setAttachments(getAttachments(dto.getAttachmentIds()));
        }

        blog.setModifiedAt(LocalDateTime.now());

        return blog;
    }

    @Transactional
    public void deleteBlog(String blogId) {

        Blog blog = blogRepository.findById(blogId);

        if (blog == null) {
            throw new NotFoundException(
                    "Blog not found: " + blogId
            );
        }

        blogRepository.delete(blog);
    }

    public List<Blog> searchBlogs(BlogSearchDTO request) {
        return blogRepository.search(request);
    }

    public List<Blog> searchBlogs(String request, int page, int pageSize) {
        return blogRepository.search(request,page,pageSize);
    }

    private List<Attachment> getAttachments(List<String> attachmentIds) {

        List<Attachment> attachments = new ArrayList<>();

        if (attachmentIds == null) {
            return attachments;
        }

        for (String attachmentId : attachmentIds) {

            Attachment attachment =
                    attachmentRepository.findById(attachmentId);

            if (attachment == null) {
                throw new NotFoundException(
                        "Attachment not found: " + attachmentId
                );
            }

            attachments.add(attachment);
        }

        return attachments;
    }
}

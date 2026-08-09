package me.hkaibni.service.media;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import me.hkaibni.dto.entity_dto.NewsDTO;
import me.hkaibni.model.media.Attachment;
import me.hkaibni.model.media.News;
import me.hkaibni.repository.media.AttachmentRepository;
import me.hkaibni.repository.media.NewsRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NewsService {

    @Inject
    NewsRepository newsRepository;

    @Inject
    AttachmentRepository attachmentRepository;

    @Inject
    JsonWebToken jwt;

    public List<News> getAllNews() {
        return newsRepository.listNews();
    }

    @Transactional
    public News getNews(UUID newsId) {

        News news = newsRepository.findById(newsId);

        if (news == null) {
            throw new NotFoundException(
                    "News not found: " + newsId
            );
        }

        return news;
    }

    @Transactional
    public News createNews(NewsDTO dto) {

        String creatorId = jwt.getSubject();

        News news = new News();

        news.setId(UUID.randomUUID().toString());
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setPublishedAt(dto.getPublishedAt());
        news.setCreatedBy(creatorId);
        news.setCreatedAt(LocalDateTime.now());
        news.setModifiedAt(LocalDateTime.now());

        news.setAttachments(
                getAttachments(dto.getAttachmentIds())
        );

        newsRepository.save(news);

        return news;
    }

    @Transactional
    public News updateNews(UUID newsId, NewsDTO dto) {

        News news = newsRepository.findById(newsId);

        if (news == null) {
            throw new NotFoundException(
                    "News not found: " + newsId
            );
        }

        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setPublishedAt(dto.getPublishedAt());

        if (dto.getAttachmentIds() != null) {
            news.setAttachments(
                    getAttachments(dto.getAttachmentIds())
            );
        }

        news.setModifiedAt(LocalDateTime.now());

        return news;
    }

    @Transactional
    public void deleteNews(UUID newsId) {

        News news = newsRepository.findById(newsId);

        if (news == null) {
            throw new NotFoundException(
                    "News not found: " + newsId
            );
        }

        newsRepository.delete(news);
    }

    private List<Attachment> getAttachments(
            List<String> attachmentIds
    ) {

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
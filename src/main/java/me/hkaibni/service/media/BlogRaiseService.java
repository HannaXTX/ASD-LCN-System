package me.hkaibni.service.media;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import me.hkaibni.dto.entity_dto.BlogRaiseDTO;
import me.hkaibni.model.media.Blog;
import me.hkaibni.model.media.BlogRaise;
import me.hkaibni.repository.media.BlogRaiseRepository;
import me.hkaibni.repository.media.BlogRepository;

import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.UUID;


@ApplicationScoped
public class BlogRaiseService {

    @Inject
    BlogRaiseRepository blogRaiseRepository;

    @Inject
    BlogRepository blogRepository;

    @Inject
    JsonWebToken jwt;


    @Transactional
    public BlogRaiseDTO raiseBlog(String blogId) {

        String actorId = jwt.getSubject();

        Blog blog = findBlog(blogId);


        if (blogRaiseRepository.hasRaised(
                blogId,
                actorId
        )) {

            throw new ClientErrorException(
                    "Blog already raised",
                    Response.Status.CONFLICT
            );
        }


        BlogRaise raise = new BlogRaise();

        raise.setId(
                UUID.randomUUID().toString()
        );

        raise.setBlog(blog);

        raise.setRaisedBy(actorId);

        raise.setCreatedAt(
                LocalDateTime.now()
        );


        blogRaiseRepository.save(raise);


        return getRaiseStatus(blogId);
    }


    @Transactional
    public BlogRaiseDTO removeRaise(
            String blogId
    ) {

        String actorId = jwt.getSubject();

        findBlog(blogId);


        long deleted =
                blogRaiseRepository
                        .deleteByBlogAndUser(
                                blogId,
                                actorId
                        );


        if (deleted == 0) {

            throw new NotFoundException(
                    "You have not raised this blog"
            );
        }


        return getRaiseStatus(blogId);
    }


    public BlogRaiseDTO getRaiseStatus(
            String blogId
    ) {

        findBlog(blogId);

        String actorId = jwt.getSubject();


        long count =
                blogRaiseRepository
                        .countByBlog(blogId);


        boolean raised =
                blogRaiseRepository
                        .hasRaised(
                                blogId,
                                actorId
                        );


        return new BlogRaiseDTO(
                blogId,
                count,
                raised
        );
    }


    private Blog findBlog(String blogId) {

        Blog blog =
                blogRepository.findById(blogId);

        if (blog == null) {

            throw new NotFoundException(
                    "Blog not found: " + blogId
            );
        }

        return blog;
    }
}
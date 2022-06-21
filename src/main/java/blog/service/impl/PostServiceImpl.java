package blog.service.impl;

import blog.entity.Post;
import blog.entity.Tag;
import blog.entity.domain.PostSummary;
import blog.exception.InvalidPostSummaryTypeException;
import blog.repository.PostRepository;
import blog.service.PostService;
import blog.service.TagService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService {
    private final PostRepository postRepository;
    private final TagService tagService;

    protected PostServiceImpl(PostRepository postRepository, TagService tagService) {
        super(postRepository);
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    @Override
    public Post findBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("No post found by this slug: " + slug));
    }

    @Override
    public List<Tag> findAllTagsById(Integer postId) {
        return findById(postId).getTags();
    }

    @Override
    public List<Tag> addTagsById(Integer postId, List<Tag> tags) {
        Post post = findById(postId);
        post.getTags().addAll(tags);
        return postRepository.save(post).getTags();
    }

    @Override
    public List<Tag> setTagsById(Integer postId, List<Tag> tags) {
        Post post = findById(postId);
        post.setTags(tags);
        return postRepository.save(post).getTags();
    }

    @Override
    public List<Tag> deleteTagsById(Integer postId, List<Tag> tags) {
        Post post = findById(postId);
        post.getTags().removeAll(tags);
        return postRepository.save(post).getTags();
    }

    @Override
    public PostSummary getSummary(PostSummary.SummaryType type) {
        List<Post> posts = findAll();
        switch (type) {
            case TAG:
                List<Tag> tags = tagService.findAll(null, null);
                return PostSummary.builder()
                        .posts(posts)
                        .tags(tags)
                        .build()
                        .byTag();
            case CATEGORY:
                return PostSummary.builder()
                        .posts(posts)
                        .build()
                        .byCategory();
            default:
                throw new InvalidPostSummaryTypeException();
        }
    }
}

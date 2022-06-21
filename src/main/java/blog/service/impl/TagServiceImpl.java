package blog.service.impl;

import blog.entity.Post;
import blog.entity.Tag;
import blog.repository.TagRepository;
import blog.repository.base.BaseRepository;
import blog.service.TagService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends BaseServiceImpl<Tag> implements TagService {

    private final TagRepository tagRepository;

    protected TagServiceImpl(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Post> findAllPostsById(Integer tagId) {
        return findById(tagId).getPosts();
    }

    @Override
    public List<Post> addPostsById(Integer tagId, List<Post> posts) {
        Tag tag = findById(tagId);
        tag.getPosts().addAll(posts);
        return tagRepository.save(tag).getPosts();
    }

    @Override
    public List<Post> setPostsById(Integer tagId, List<Post> posts) {
        Tag tag = findById(tagId);
        tag.setPosts(posts);
        return tagRepository.save(tag).getPosts();
    }

    @Override
    public List<Post> deletePostsById(Integer tagId, List<Post> posts) {
        Tag tag = findById(tagId);
        tag.getPosts().removeAll(posts);
        return tagRepository.save(tag).getPosts();
    }
}

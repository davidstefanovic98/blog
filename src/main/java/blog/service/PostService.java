package blog.service;

import blog.entity.Post;
import blog.entity.Tag;
import blog.entity.domain.PostSummary;
import blog.service.base.BaseService;

import java.util.List;

public interface PostService extends BaseService<Post> {

    Post findBySlug(String slug);

    List<Tag> findAllTagsById(Integer postId);

    List<Tag> addTagsById(Integer postId, List<Tag> tags);

    List<Tag> setTagsById(Integer postId, List<Tag> tags);

    List<Tag> deleteTagsById(Integer postId, List<Tag> tags);

    PostSummary getSummary(PostSummary.SummaryType type);
}

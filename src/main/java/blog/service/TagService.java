package blog.service;

import blog.entity.Post;
import blog.entity.Tag;
import blog.service.base.BaseService;

import java.util.List;

public interface TagService extends BaseService<Tag> {

    List<Post> findAllPostsById(Integer tagId);

    List<Post> addPostsById(Integer tagId, List<Post> posts);

    List<Post> setPostsById(Integer tagId, List<Post> posts);

    List<Post> deletePostsById(Integer tagId, List<Post> posts);
}

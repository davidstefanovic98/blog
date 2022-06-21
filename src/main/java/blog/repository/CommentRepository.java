package blog.repository;

import blog.entity.Comment;
import blog.entity.Post;
import blog.repository.base.BaseRepository;

import java.util.List;

public interface CommentRepository extends BaseRepository<Comment> {

    List<Comment> findAllByPost(Post post);
}

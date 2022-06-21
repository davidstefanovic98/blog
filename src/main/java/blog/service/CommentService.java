package blog.service;

import blog.entity.Comment;
import blog.entity.Post;
import blog.service.base.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CommentService extends BaseService<Comment> {

    List<Comment> findAllByPost(Post post);

    List<Comment> findAll(Integer postId, Specification<Comment> specification, Pageable pageable);

    Comment save(Integer postId, Comment comment);
}

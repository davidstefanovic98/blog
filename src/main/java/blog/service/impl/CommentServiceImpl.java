package blog.service.impl;

import blog.entity.Comment;
import blog.entity.Post;
import blog.exception.CommentEmptyException;
import blog.repository.CommentRepository;
import blog.service.CommentService;
import blog.service.NotificationService;
import blog.service.PostService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static blog.specification.SearchOperation.IS_NULL;
import static blog.util.SortUtil.overrideSort;
import static blog.util.SpecificationUtil.combineSpecificationFor;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final NotificationService notificationService;

    protected CommentServiceImpl(CommentRepository commentRepository, PostService postService, NotificationService notificationService) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.notificationService = notificationService;
    }

    @Override
    public List<Comment> findAllByPost(Post post) {
        return commentRepository.findAllByPost(post);
    }

    @Override
    public List<Comment> findAll(Integer postId, Specification<Comment> specification, Pageable pageable) {
        specification = combineSpecificationFor(specification, "post.id", postId);
        specification = combineSpecificationFor(specification, "parent", IS_NULL);
        Sort defaultSort = Sort.by("createdDate").descending();
        pageable = overrideSort(pageable, defaultSort);
        if (pageable == null)
            return commentRepository.findAll(specification, defaultSort);
        return commentRepository.findAll(specification, pageable).toList();
    }

    @Override
    public Comment save(Integer postId, Comment comment) {
        Post post = postService.findById(postId);
        if (comment.getBody() == null || comment.getBody().isEmpty()) {
            throw new CommentEmptyException();
        }
        comment.setPost(post);
        try {
            return commentRepository.save(comment);
        } finally {
            notificationService.createForComment(post, comment);
        }
    }

}

package blog.service.impl;

import blog.entity.Comment;
import blog.entity.Notification;
import blog.entity.Post;
import blog.entity.User;
import blog.exception.HttpUnauthorizedException;
import blog.repository.NotificationRepository;
import blog.repository.base.BaseRepository;
import blog.service.CommentService;
import blog.service.NotificationService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification> implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CommentService commentService;

    protected NotificationServiceImpl(NotificationRepository notificationRepository, CommentService commentService) {
        super(notificationRepository);
        this.notificationRepository = notificationRepository;
        this.commentService = commentService;
    }

    @Override
    public List<Notification> findAllUnreadForUser(User user, Pageable pageable) {
        return notificationRepository.findAllByUserAndReadFalseOrderByCreatedDateDesc(user, pageable);
    }

    @Override
    public List<Notification> findAllForUser(User user, Pageable pageable) {
        return notificationRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);
    }

    @Override
    public void createForComment(Post post, Comment comment) {
        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Notification notification = new Notification();
        notification.setTitle(String.format("%s commented on post %s", comment.getUser().getDisplayName(), post.getTitle()));
        notification.setBody(comment.getBody());
        notification.setActionUrl("/posts/" + post.getSlug() + "#comments");
        commentService.findAllByPost(post)
                .stream()
                .map(Comment::getUser)
                .distinct()
                .filter(user -> !user.equals(authenticated))
                .forEach(user -> {
                    Notification notificationCopy = new Notification(notification);
                    notificationCopy.setUser(user);
                    save(notificationCopy);
                });
        if (!post.getUser().equals(authenticated)) {
            Notification notificationCopy = new Notification(notification);
            notificationCopy.setUser(post.getUser());
            save(notificationCopy);
        }
    }

    @Override
    public void markAsRead(User user, Integer notificationId) {
        Notification notification = findById(notificationId);
        if (!notification.getUser().equals(user))
            throw new HttpUnauthorizedException();
        notification.setRead(true);
        update(notification);
    }
}

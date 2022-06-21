package blog.service;

import blog.entity.Comment;
import blog.entity.Notification;
import blog.entity.Post;
import blog.entity.User;
import blog.service.base.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService extends BaseService<Notification> {

    List<Notification> findAllUnreadForUser(User user, Pageable pageable);

    List<Notification> findAllForUser(User user, Pageable pageable);

    void createForComment(Post post, Comment comment);

    void markAsRead(User user, Integer notificationId);

}

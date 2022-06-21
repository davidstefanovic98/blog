package blog.repository;

import blog.entity.Notification;
import blog.entity.User;
import blog.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationRepository extends BaseRepository<Notification> {

    List<Notification> findAllByUserAndReadFalseOrderByCreatedDateDesc(User user, Pageable pageable);

    List<Notification> findAllByUserOrderByCreatedDateDesc(User user, Pageable pageable);
}

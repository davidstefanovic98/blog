package blog.repository;

import blog.entity.User;
import blog.repository.base.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUsername(String username);
}

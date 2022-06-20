package blog.repository;

import blog.entity.Role;
import blog.repository.base.BaseRepository;

import java.util.Optional;

public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByName(String name);
}

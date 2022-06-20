package blog.service;

import blog.entity.Role;
import blog.entity.User;
import blog.service.base.BaseService;

import java.util.List;

public interface RoleService extends BaseService<Role> {

    List<User> findAllUsersById(Integer roleId);

    Role findByName(String name);
}

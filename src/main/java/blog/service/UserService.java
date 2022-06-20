package blog.service;

import blog.data.ChangePasswordDTO;
import blog.data.RegisterUserDTO;
import blog.entity.Role;
import blog.entity.User;
import blog.service.base.BaseService;

import java.util.List;

public interface UserService extends BaseService<User> {

    List<Role> findAllRolesById(Integer userId);

    User findByUsername(String username);

    void changePassword(User user, ChangePasswordDTO passwordDto);

    void disableUser(Integer userId);

    User register(RegisterUserDTO dto);

    User resetPassword(Integer userId);

    void enableUser(Integer userId);
}

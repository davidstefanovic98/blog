package blog.service.impl;

import blog.entity.Role;
import blog.entity.User;
import blog.repository.RoleRepository;
import blog.service.RoleService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;

    protected RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAllUsersById(Integer roleId) {
        return findById(roleId).getUsers();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("No role found with this name: " + name));
    }
}

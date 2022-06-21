package blog.controller;

import blog.entity.Role;
import blog.entity.User;
import blog.service.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllRoles")
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{roleId}")
    @ApiOperation(value = "", nickname = "getRoleById")
    public ResponseEntity<Role> getById(@PathVariable Integer roleId) {
        return ResponseEntity.ok(roleService.findById(roleId));
    }

    @PostMapping
    @ApiOperation(value = "", nickname = "saveRole")
    public ResponseEntity<Role> save(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.save(role));
    }

    @PutMapping
    @ApiOperation(value = "", nickname = "updateRole")
    public ResponseEntity<Role> update(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.update(role));
    }

    @DeleteMapping("/{roleId}")
    @ApiOperation(value = "", nickname = "deleteRoleById")
    public void deleteById(@PathVariable Integer roleId) {
        roleService.deleteById(roleId);
    }

    @GetMapping("/{roleId}/users")
    @ApiOperation(value = "", nickname = "getAllRoleUsers")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable Integer roleId) {
        return ResponseEntity.ok(roleService.findAllUsersById(roleId));
    }
}

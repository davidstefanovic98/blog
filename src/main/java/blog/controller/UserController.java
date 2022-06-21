package blog.controller;

import blog.data.ChangePasswordDTO;
import blog.data.RegisterUserDTO;
import blog.entity.Notification;
import blog.entity.Role;
import blog.entity.User;
import blog.exception.HttpUnauthorizedException;
import blog.service.NotificationService;
import blog.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(name = "q", required = false) Specification<User> specification,
                                                  @RequestParam(name = "page", required = false) Pageable pageable,
                                                  @RequestParam(name = "sort", required = false) Sort sort) {
        return ResponseEntity.ok(userService.findAll(specification, pageable, sort));
    }

    @GetMapping("/{identifier}")
    @ApiOperation(value = "", nickname = "getUserById")
    public ResponseEntity<User> getById(@PathVariable String identifier) {
        try {
            return ResponseEntity.ok(userService.findById(Integer.parseInt(identifier)));
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(userService.findByUsername(identifier));
        }
    }

    @GetMapping("/notifications")
    @ApiOperation(value = "", nickname = "getNotificationsForUser")
    public ResponseEntity<List<Notification>> getNotificationsForUser(@AuthenticationPrincipal User user,
                                                                      @RequestParam(required = false, name = "all") Boolean all,
                                                                      @RequestParam(name = "page", required = false) Pageable pageable) {
        if (all != null && all)
            return ResponseEntity.ok(notificationService.findAllForUser(user, pageable));
        return ResponseEntity.ok(notificationService.findAllUnreadForUser(user, pageable));
    }

    @PostMapping
    @ApiOperation(value = "", nickname = "saveUser")
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.status(CREATED).body(userService.save(user));
    }

    @PostMapping("/register")
    @ApiOperation(value = "", nickname = "registerUser")
    public ResponseEntity<User> save(@RequestBody RegisterUserDTO dto) {
        return ResponseEntity.status(CREATED).body(userService.register(dto));
    }

    @PutMapping
    @ApiOperation(value = "", nickname = "updateUser")
    public ResponseEntity<User> update(@AuthenticationPrincipal User auth, @RequestBody User user) {
        if (!auth.isAdmin()) {
            if (!auth.equals(user))
                throw new HttpUnauthorizedException();
            user.setRoles(auth.getRoles());
        }
        return ResponseEntity.ok(userService.update(user));
    }

    @PutMapping("/password")
    @ApiOperation(value = "", nickname = "updateUserPassword")
    public void updateUserPassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordDTO passwordDto) {
        userService.changePassword(user, passwordDto);
    }

    @DeleteMapping("/{userId}/password")
    @ApiOperation(value = "", nickname = "resetUserPassword")
    public ResponseEntity<User> resetUserPassword(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.resetPassword(userId));
    }

    @PutMapping("/{userId}/enable")
    @ApiOperation(value = "", nickname = "enableUser")
    @ResponseStatus(code = OK)
    public void enableUser(@PathVariable Integer userId) {
        userService.enableUser(userId);
    }

    @DeleteMapping("/{userId}/enable")
    @ApiOperation(value = "", nickname = "disableUser")
    @ResponseStatus(code = OK)
    public void disableUser(@PathVariable Integer userId) {
        userService.disableUser(userId);
    }

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "", nickname = "deleteUserById")
    public void deleteById(@PathVariable Integer userId) {
        userService.deleteById(userId);
    }

    @GetMapping("/{userId}/roles")
    @ApiOperation(value = "", nickname = "getAllUserRoles")
    public ResponseEntity<List<Role>> getAllRoles(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.findAllRolesById(userId));
    }

}

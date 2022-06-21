package blog.controller;

import blog.entity.User;
import blog.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PutMapping("/{notificationId}/read")
    @ApiOperation(value = "", nickname = "markAsRead")
    public void markAsRead(@AuthenticationPrincipal User user,
                           @PathVariable Integer notificationId) {
        notificationService.markAsRead(user, notificationId);
    }
}

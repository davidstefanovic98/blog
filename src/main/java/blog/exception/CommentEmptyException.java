package blog.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@ResponseStatus(code = BAD_GATEWAY, reason = "comment.save.body-empty")
public class CommentEmptyException extends RuntimeException{
}

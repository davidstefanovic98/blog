package blog.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Access is denied")
@NoArgsConstructor
public class HttpUnauthorizedException extends RuntimeException{
}

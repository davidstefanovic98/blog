package blog.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code = BAD_REQUEST, reason = "post.summary.invalid-type")
public class InvalidPostSummaryTypeException extends RuntimeException {
}

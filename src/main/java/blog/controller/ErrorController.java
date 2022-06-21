package blog.controller;
import blog.data.ResponseError;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Properties;

@ControllerAdvice
@Component
public class ErrorController {
    @Resource
    private Properties properties;

    @ExceptionHandler({
            NoSuchElementException.class,
            UsernameNotFoundException.class
    })
    protected ResponseEntity<ResponseError> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getResponseError(getErrorMessage(ex), request, status);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            SQLIntegrityConstraintViolationException.class,
            DataIntegrityViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
    })
    protected ResponseEntity<ResponseError> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        if (ex instanceof DataIntegrityViolationException) {
            Throwable rootCause = ((DataIntegrityViolationException) ex).getRootCause();
            if (rootCause != null && rootCause.getMessage().contains("Cannot delete or update a parent row")) {
                message = "error.cannotDelete";
            } else if (rootCause instanceof SQLException) {
                message = rootCause.getMessage();
            }
        } else if (ex instanceof  HttpMessageNotReadableException){
            if (ex.getCause() instanceof UnrecognizedPropertyException) {
                String propertyName = ((UnrecognizedPropertyException) ex.getCause()).getPropertyName();
                String className = ((UnrecognizedPropertyException) ex.getCause()).getReferringClass().getSimpleName();
                message = String.format("Unrecognized property '%s' in type '%s'", propertyName, className);
            }
        }
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getResponseError(getErrorMessage(message), request, status);
    }

    @ExceptionHandler({
            MaxUploadSizeExceededException.class
    })
    protected ResponseEntity<ResponseError> handlePayloadTooLarge(RuntimeException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.PAYLOAD_TOO_LARGE;
        return getResponseError(getErrorMessage(message), request, status);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    protected ResponseEntity<ResponseError> handleForbidden(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return getResponseError(getErrorMessage(ex), request, status);
    }

    @ExceptionHandler({
            Exception.class
    })
    protected ResponseEntity<ResponseError> handleInternalServerError(RuntimeException ex, HttpServletRequest request) {
        ex.printStackTrace();
        //
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            String message = responseStatus.reason();
            if (ex.getMessage() != null)
                message = String.format("%s: %s", responseStatus.reason(), ex.getMessage());

            return getResponseError(message, request, responseStatus.code());
        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return getResponseError(getErrorMessage(ex), request, status);
    }

    private String getErrorMessage(String messageProp) {
        if (messageProp == null)
            messageProp = "error.generic";

        String message = properties.getProperty(messageProp);
        return message == null ? messageProp : message;
    }

    private ResponseEntity<ResponseError> getResponseError(String message, HttpServletRequest request, HttpStatus status) {
        ResponseError errorInfo = new ResponseError(status, request, message);
        return ResponseEntity.status(errorInfo.getCode()).body(errorInfo);
    }

    private String getErrorMessage(Exception exception) {
        return getErrorMessage(exception.getMessage());
    }
}

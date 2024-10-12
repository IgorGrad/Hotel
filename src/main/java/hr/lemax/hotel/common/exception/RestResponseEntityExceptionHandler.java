package hr.lemax.hotel.common.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A controller advice to handle various exceptions globally within the application and provide
 * appropriate HTTP responses for different types of errors.
 */
@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle exceptions when an entity is not found, including {@link NoSuchElementException}
     * and {@link EntityNotFoundException}.
     *
     * @param e the exception that was thrown
     * @param request the current request
     * @return a response entity with a detailed error message and a 404 Not Found status
     */
    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(
            final Exception e,
            final WebRequest request) {
        log.debug(e.getClass().getSimpleName() + " thrown. Message: {}", e.getMessage());

        final String responseError = extractResponseError(e.getMessage());
        final String message = String.format(e.getClass().getSimpleName() + ": %s", responseError);

        final HttpHeaders headers = new HttpHeaders();
        final ProblemDetail body = createProblemDetail(
                e,
                HttpStatus.NOT_FOUND,
                message,
                null,
                null,
                request);
        return handleExceptionInternal(e, body, headers, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handle {@link DataIntegrityViolationException} which usually occurs when there are
     * violations of database constraints (e.g., unique constraints).
     *
     * @param e the data integrity violation exception
     * @param request the current request
     * @return a response entity with a detailed error message and a 400 Bad Request status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            final DataIntegrityViolationException e,
            final WebRequest request) {
        log.debug("DataIntegrityViolationException thrown. Message: {}", e.getMessage());

        final String responseError = extractResponseError(e.getMessage());
        final String message = String.format("DataIntegrityViolationException: %s", responseError);

        final HttpHeaders headers = new HttpHeaders();
        final ProblemDetail body = createProblemDetail(
                e,
                HttpStatus.BAD_REQUEST,
                message,
                null,
                null,
                request);
        return handleExceptionInternal(e, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handle cases where an incoming HTTP request message is not readable or malformed.
     *
     * @param e the exception indicating that the message is unreadable
     * @param headers the HTTP headers
     * @param status the HTTP status
     * @param request the current request
     * @return a response entity with a detailed error message and the appropriate status code
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request) {
        log.debug("HttpMessageNotReadableException thrown. Message: {}", e.getMessage());
        final String responseError = extractResponseError(e.getMessage());
        final String message = String.format("DataIntegrityViolationException: %s", responseError);

        final ProblemDetail body = createProblemDetail(
                e,
                status,
                message,
                null,
                null,
                request);
        return handleExceptionInternal(e, body, headers, status, request);
    }

    /**
     * Handle validation errors for method arguments, especially for cases where bean validation
     * (e.g., `@Valid` annotations) fails.
     *
     * @param e the exception indicating invalid method arguments
     * @param headers the HTTP headers
     * @param status the HTTP status code
     * @param request the current request
     * @return a response entity with a list of validation errors and the appropriate status code
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request) {
        log.debug("MethodArgumentNotValidException thrown. Message: {}", e.getMessage());

        final List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        final ProblemDetail body = createProblemDetail(
                e,
                status,
                String.join(", ", errors),
                null,
                null,
                request);
        return handleExceptionInternal(e, body, headers, status, request);
    }

    /**
     * Extracts the error message from the given string, typically based on a keyword or format.
     *
     * @param error the error message to process
     * @return the extracted portion of the error message or the original message if no match is found
     */
    private String extractResponseError(final String error) {
        final int startIndex = error.indexOf("Detail:");
        final int endIndex = error.indexOf("]");
        if (startIndex >= 0 && endIndex >= 0) {
            return error.substring(startIndex, endIndex);
        }
        if (startIndex >= 0) {
            return error.substring(startIndex);
        }
        if (endIndex >= 0) {
            return error.substring(0, endIndex);
        }
        return error;
    }
}

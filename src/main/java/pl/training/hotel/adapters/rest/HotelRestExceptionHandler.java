package pl.training.hotel.adapters.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.training.common.aop.ThrottlingException;
import pl.training.hotel.adapters.StayPolicyViolationException;
import pl.training.hotel.application.RoomNotFoundException;
import pl.training.hotel.domain.InvalidReservationPeriodException;
import pl.training.hotel.domain.RoomCapacityExceededException;
import pl.training.hotel.domain.RoomNotAvailableException;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
class HotelRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String RETRY_AFTER_IN_SECONDS = "1";
    private static final String ERRORS_PROPERTY = "errors";
    private static final String TIMESTAMP_PROPERTY = "timestamp";

    @ExceptionHandler(RoomNotFoundException.class)
    ResponseEntity<Object> onRoomNotFound(RoomNotFoundException exception, WebRequest request) {
        return problem(exception, NOT_FOUND, new HttpHeaders(), request);
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    ResponseEntity<Object> onRoomNotAvailable(RoomNotAvailableException exception, WebRequest request) {
        return problem(exception, CONFLICT, new HttpHeaders(), request);
    }

    @ExceptionHandler({RoomCapacityExceededException.class, InvalidReservationPeriodException.class, StayPolicyViolationException.class})
    ResponseEntity<Object> onReservationRejected(RuntimeException exception, WebRequest request) {
        return problem(exception, UNPROCESSABLE_CONTENT, new HttpHeaders(), request);
    }

    @ExceptionHandler(ThrottlingException.class)
    ResponseEntity<Object> onThrottling(ThrottlingException exception, WebRequest request) {
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.RETRY_AFTER, RETRY_AFTER_IN_SECONDS);
        return problem(exception, TOO_MANY_REQUESTS, headers, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        exception.getBody().setProperty(ERRORS_PROPERTY, describe(exception.getBindingResult()));
        return super.handleMethodArgumentNotValid(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException exception, HttpHeaders headers,
                                                                            HttpStatusCode status, WebRequest request) {
        var errors = exception.getParameterValidationResults().stream()
                .flatMap(result -> result instanceof ParameterErrors parameterErrors
                        ? describe(parameterErrors).stream()
                        : result.getResolvableErrors().stream()
                        .map(error -> result.getMethodParameter().getParameterName() + ": " + error.getDefaultMessage()))
                .toList();
        exception.getBody().setProperty(ERRORS_PROPERTY, errors);
        return super.handleHandlerMethodValidationException(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (body instanceof ProblemDetail problemDetail) {
            problemDetail.setProperty(TIMESTAMP_PROPERTY, Instant.now());
        }
        return super.createResponseEntity(body, headers, statusCode, request);
    }

    private ResponseEntity<Object> problem(Exception exception, HttpStatus status, HttpHeaders headers, WebRequest request) {
        var problemDetail = createProblemDetail(exception, status, exception.getClass().getSimpleName(), null, null, request);
        return handleExceptionInternal(exception, problemDetail, headers, status, request);
    }

    private List<String> describe(Errors errors) {
        return errors.getAllErrors().stream()
                .map(error -> error instanceof FieldError fieldError
                        ? fieldError.getField() + ": " + error.getDefaultMessage()
                        : error.getDefaultMessage())
                .toList();
    }

}

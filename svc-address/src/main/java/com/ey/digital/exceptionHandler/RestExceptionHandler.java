package com.ey.digital.exceptionHandler;

import com.ey.digital.Util.Constants;
import com.ey.digital.exception.EntityNotFoundException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(MultipartException.class)
  protected ResponseEntity<Object> handleMultipartException(MultipartException ex) {
    RestEndPointErrorWrapper restEndPointErrorWrapper =
        new RestEndPointErrorWrapper(HttpStatus.PARTIAL_CONTENT);
    restEndPointErrorWrapper.setMessage(Constants.MULTIPART_FAIL);
    restEndPointErrorWrapper.setDebugMessage(ex.getMessage());
    return buildResponseEntity(restEndPointErrorWrapper);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
    RestEndPointErrorWrapper restEndPointErrorWrapper =
        new RestEndPointErrorWrapper(HttpStatus.NOT_FOUND);
    restEndPointErrorWrapper.setMessage(Constants.ENTITY_NOT_FOUND);
    restEndPointErrorWrapper.setDebugMessage(ex.getMessage());
    return buildResponseEntity(restEndPointErrorWrapper);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex) {
    RestEndPointErrorWrapper restEndPointErrorWrapper =
        new RestEndPointErrorWrapper(HttpStatus.BAD_REQUEST);
    String errorMessage =
        ex.getConstraintViolations()
            .stream()
            .map(
                constraintViolation ->
                    String.format(
                        "'%s' : %s",
                        ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName(),
                        constraintViolation.getMessage()))
            .collect(Collectors.joining(", "));
    restEndPointErrorWrapper.setMessage(errorMessage);
    restEndPointErrorWrapper.setDebugMessage(errorMessage);
    return buildResponseEntity(restEndPointErrorWrapper);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    RestEndPointErrorWrapper restEndPointErrorWrapper =
        new RestEndPointErrorWrapper(HttpStatus.METHOD_NOT_ALLOWED);
    restEndPointErrorWrapper.setMessage(ex.getMethod() + Constants.METHOD_NOT_SUPPORTED);
    restEndPointErrorWrapper.setDebugMessage(ex.getMessage());
    return buildResponseEntity(restEndPointErrorWrapper);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String errorMessage =
        ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
    RestEndPointErrorWrapper restEndPointErrorWrapper =
        new RestEndPointErrorWrapper(HttpStatus.BAD_REQUEST);
    restEndPointErrorWrapper.setMessage(errorMessage);
    restEndPointErrorWrapper.setDebugMessage(errorMessage);
    return buildResponseEntity(restEndPointErrorWrapper);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception ex) {
    RestEndPointErrorWrapper restEndPointErrorWrapper =
        new RestEndPointErrorWrapper(HttpStatus.INTERNAL_SERVER_ERROR);
    restEndPointErrorWrapper.setMessage(Constants.INTERLA_SERVICE_ERROR_RESPONSE);
    restEndPointErrorWrapper.setDebugMessage(ex.getMessage());
    return buildResponseEntity(restEndPointErrorWrapper);
  }

  private ResponseEntity<Object> buildResponseEntity(
      RestEndPointErrorWrapper restEndPointErrorWrapper) {
    return new ResponseEntity<>(restEndPointErrorWrapper, restEndPointErrorWrapper.getStatus());
  }
}

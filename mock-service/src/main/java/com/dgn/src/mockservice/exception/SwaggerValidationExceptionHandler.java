package com.dgn.src.mockservice.exception;

import com.dgn.src.common.apierror.handler.CommonsExceptionHandler;
import com.dgn.src.mockservice.model.ResponseHeader;
import com.dgn.src.mockservice.model.SRCAccountProvisionResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import java.util.*;


@ControllerAdvice
@RestController
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwaggerValidationExceptionHandler extends CommonsExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String sessionId = request.getSessionId();
        SRCAccountProvisionResponse response = new SRCAccountProvisionResponse();
        response.setErrors(new ArrayList());
        ResponseHeader responseHeader = new ResponseHeader();
        response.setResponseHeader(responseHeader);
        responseHeader.setResponseId(UUID.randomUUID().toString());
        responseHeader.setSessionId(sessionId);
        if (ex.getBindingResult() != null && ex.getBindingResult().getFieldErrors().size() > 0) {
            List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
            fieldErrorList.stream().forEach((fieldError) -> {
                String defaultMessage = fieldError.getDefaultMessage();
                String errorField = fieldError.getField();
                String errorMessage = errorField + " - " + defaultMessage;
                com.dgn.src.mockservice.model.Error responseErr = new com.dgn.src.mockservice.model.Error();
                responseErr.setErrorCode("10001");
                responseErr.setErrorMessage(errorMessage);
                response.getErrors().add(responseErr);
            });
        }
         return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleException(Exception ex) {
        SRCAccountProvisionResponse response = new SRCAccountProvisionResponse();
        response.setErrors(new ArrayList());
        ResponseHeader responseHeader = new ResponseHeader();
        response.setResponseHeader(responseHeader);
        responseHeader.setResponseId(UUID.randomUUID().toString());
        String errorMessage = null;
        if (ex.getCause()!= null) {
            errorMessage = ex.getCause().getMessage();
        } else {
            errorMessage = "internal server error";
        }
        com.dgn.src.mockservice.model.Error responseErr = new com.dgn.src.mockservice.model.Error();
        responseErr.setErrorCode("10001");
        responseErr.setErrorMessage(errorMessage);
        response.getErrors().add(responseErr);
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

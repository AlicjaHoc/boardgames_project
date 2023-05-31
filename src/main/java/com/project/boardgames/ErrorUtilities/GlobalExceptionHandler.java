package com.project.boardgames.ErrorUtilities;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<RequestResponse<Object>> handleAppException(AppException ex) {
        RequestResponse<Object> response;
        if (ex.getStatusCode() == 500) {
            // If the exception has a status code of 500, include the full stack trace
            response = new RequestResponse<>(false, null, ex.getMessage(), ex);
        } else {
            // For other exceptions, return a generic error message without the full stack trace
            AppException appEx = new AppException("An error occurred while processing the request.", 500, "error", false);
            response = new RequestResponse<>(false, null, ex.getMessage(), appEx);
        }
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RequestResponse<Object>> handleGenericException(Exception ex) {
        AppException appEx = new AppException("An error occurred while processing the request.", 500, "error", false);
        RequestResponse<Object> response = new RequestResponse<>(false, null, ex.getMessage(), appEx);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

package com.project.boardgames.ErrorUtilities;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AppException.class)
    public ResponseEntity<RequestResponse<Object>> handleAppException(AppException ex) {
        RequestResponse<Object> response = new RequestResponse<>(false, null,null, ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<RequestResponse<Object>> handleGenericException(Exception ex) {
        AppException appEx = new AppException("An error occurred while processing the request.", 500, "error", true);
        RequestResponse<Object> response = new RequestResponse<>(false, null, ex.getMessage(), appEx);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

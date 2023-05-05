package com.project.boardgames.ErrorUtilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public class AppException extends RuntimeException {

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public boolean isOperational() {
        return isOperational;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public boolean isStackIncluded() {
        return isStackIncluded;
    }

    public void setStackIncluded(boolean stackIncluded) {
        isStackIncluded = stackIncluded;
    }
    @Value("${app.exception.show-stack}")
    private boolean showStackTrace;

    @Override
    public synchronized Throwable fillInStackTrace() {
        if (showStackTrace) {
            return super.fillInStackTrace();
        } else {
            return null;
        }
    }
    private final int statusCode;
    private final String status;
    private final boolean isOperational;
    private final String message;
    private boolean isStackIncluded;

    public AppException(String message, int statusCode, String status, boolean isOperational) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
        this.isOperational = isOperational;
        this.isStackIncluded = false;
    }

}
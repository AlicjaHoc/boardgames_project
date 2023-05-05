package com.project.boardgames.utilities;

import com.project.boardgames.ErrorUtilities.AppException;

public class RequestResponse<T> {

    private final boolean success;
    private final T data;
    private final AppException error;
    private final String message;

    public RequestResponse(boolean success, T data, String message) {
        this(success, data, message, null);
    }

    public RequestResponse(boolean success, T data, String message, AppException error) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public AppException getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}


package com.livrotech.dto;

public class ApiExceptionDTO {

    private int status;
    private String message;
    private String field;

    public ApiExceptionDTO(int status, String message, String field) {
        this.status = status;
        this.message = message;
        this.field = field;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}
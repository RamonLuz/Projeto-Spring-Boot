package com.livrotech.entity;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int status;
	private final String field;

	public ApiException(int status, String message, String field) {
        super(message);
        this.status = status;
        this.field = field;
    }

	public int getStatus() {
		return status;
	}

	public String getField() {
		return field;
	}

}

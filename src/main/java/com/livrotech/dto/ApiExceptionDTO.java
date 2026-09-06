package com.livrotech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiExceptionDTO {

    private int status;
    private String message;
    private String field;
}
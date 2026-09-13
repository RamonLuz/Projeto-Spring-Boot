package com.livrotech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiExceptionDTO {

    private int status;
    @NotBlank
    private String message;
    private String field;
    private List<ApiValidationErrorDTO> errors;

    public ApiExceptionDTO(int status, String message, String field) {
        this(status, message, field, null);
    }

    public ApiExceptionDTO(int status, String message, String field, List<ApiValidationErrorDTO> errors) {
        this.status = status;
        this.message = message;
        this.field = field;
        this.errors = errors;
    }
}
package com.livrotech.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

import com.livrotech.dto.ApiExceptionDTO;
import com.livrotech.dto.ApiValidationErrorDTO;
import com.livrotech.entity.ApiException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiExceptionDTO> handleArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        ApiExceptionDTO response = new ApiExceptionDTO(
                400,
                "Parâmetro inválido",
                "parameter"
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDTO> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiValidationErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();
        ApiValidationErrorDTO firstError = errors.stream().findFirst()
                .orElse(new ApiValidationErrorDTO("body", "Dados inválidos"));

        ApiExceptionDTO response = new ApiExceptionDTO(
                400,
                firstError.message(),
                firstError.field(),
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiExceptionDTO> handleConstraintViolation(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations().stream()
                .map(error -> new ApiValidationErrorDTO(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
        ApiValidationErrorDTO firstError = errors.stream().findFirst()
                .orElse(new ApiValidationErrorDTO("parameter", "Parâmetro inválido"));
        ApiExceptionDTO response = new ApiExceptionDTO(
                400,
                firstError.message(),
                firstError.field(),
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiExceptionDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        ApiExceptionDTO response = new ApiExceptionDTO(
                409,
                "A operação viola uma regra de integridade dos dados",
                "data"
        );

        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionDTO> handleBodyMissing(HttpMessageNotReadableException ex) {
        ApiExceptionDTO response = new ApiExceptionDTO(
                400,
                "Body da requisição está ausente ou inválido",
                "body"
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionDTO> handleApiException(ApiException ex) {
        ApiExceptionDTO response = new ApiExceptionDTO(
                ex.getStatus(),
                ex.getMessage(),
                ex.getField()
        );

        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
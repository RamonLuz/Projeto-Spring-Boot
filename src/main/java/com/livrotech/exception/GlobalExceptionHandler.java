package com.livrotech.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

import com.livrotech.dto.ApiExceptionDTO;
import com.livrotech.dto.ApiValidationErrorDTO;

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
        String databaseMessage = ex.getMostSpecificCause().getMessage();
        String message = "A operação viola uma regra de integridade dos dados";
        String field = "data";

        if (databaseMessage != null) {
            String normalizedMessage = databaseMessage.toUpperCase();
            if (normalizedMessage.contains("FK_SALES_BOOK")) {
                message = "Não é possível remover um livro que possui vendas";
                field = "bookId";
            } else if (normalizedMessage.contains("FK_SALES_CUSTOMER")) {
                message = "Não é possível remover um cliente que possui vendas";
                field = "customerId";
            } else if (normalizedMessage.contains("FK_SALES_EMPLOYEE")) {
                message = "Não é possível remover um funcionário que possui vendas";
                field = "employeeId";
            }
        }

        ApiExceptionDTO response = new ApiExceptionDTO(
                409,
                message,
                field
        );

        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiExceptionDTO> handleOptimisticLocking(
            ObjectOptimisticLockingFailureException ex) {
        ApiExceptionDTO response = new ApiExceptionDTO(
                409,
                "O registro foi alterado por outro usuário. Tente novamente",
                "data"
        );

        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionDTO> handleUnreadableBody(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof JsonParseException) {
            return ResponseEntity.badRequest().body(new ApiExceptionDTO(
                    400,
                    "JSON malformado",
                    "body"
            ));
        }

        if (cause instanceof InvalidFormatException invalidFormatException) {
            String field = invalidFormatException.getPath().stream()
                    .reduce((first, second) -> second)
                    .map(reference -> reference.getFieldName())
                    .orElse("body");

            return ResponseEntity.badRequest().body(new ApiExceptionDTO(
                    400,
                    "Valor inválido para o campo '" + field + "'",
                    field
            ));
        }

        if (ex.getMessage() != null && ex.getMessage().contains("Required request body")) {
            return ResponseEntity.badRequest().body(new ApiExceptionDTO(
                    400,
                    "Body da requisição é obrigatório",
                    "body"
            ));
        }

        ApiExceptionDTO response = new ApiExceptionDTO(
                400,
                "Body da requisição contém dados inválidos",
                "body"
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiExceptionDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ApiExceptionDTO response = new ApiExceptionDTO(
                400,
                ex.getMessage(),
                "data"
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
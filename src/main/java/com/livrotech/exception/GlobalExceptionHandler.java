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
import com.livrotech.entity.ApiException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiExceptionDTO> argumentException(
	        Exception ex) {

	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            "Parâmetro inválido",
	            "PARAMETER"
	    );

	    return ResponseEntity
	            .badRequest()
	            .body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiExceptionDTO> handleValidation(MethodArgumentNotValidException ex) {
	    var error = ex.getBindingResult().getFieldErrors().stream().findFirst();
	    String field = error.map(fieldError -> fieldError.getField()).orElse("BODY");
	    String message = error.map(fieldError -> fieldError.getDefaultMessage())
	            .orElse("Dados inválidos");

	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            message,
	            field
	    );

	    return ResponseEntity
	            .badRequest()
	            .body(response);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiExceptionDTO> handleConstraintViolation(ConstraintViolationException ex) {
	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            "Parâmetro inválido",
	            "PARAMETER"
	    );

	    return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiExceptionDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
	    ApiExceptionDTO response = new ApiExceptionDTO(
	            409,
	            "A operação viola uma regra de integridade dos dados",
	            "DATA"
	    );

	    return ResponseEntity.status(409).body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiExceptionDTO> handleBodyMissing(
	        HttpMessageNotReadableException ex) {

	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            "Body da requisição está ausente ou inválido",
	            "BODY"
	    );

	    return ResponseEntity
	            .badRequest()
	            .body(response);
	}
	
	 @ExceptionHandler(ApiException.class)
	    public ResponseEntity<?> handleApiException(ApiException ex) {

	        ApiExceptionDTO response = new ApiExceptionDTO(
	                ex.getStatus(),
	                ex.getMessage(),
	                ex.getField()
	        );

	        return ResponseEntity
	                .status(ex.getStatus())
	                .body(response);
	    }
}
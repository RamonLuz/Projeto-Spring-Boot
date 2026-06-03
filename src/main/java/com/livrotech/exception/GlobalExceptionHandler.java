package com.livrotech.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.livrotech.dto.ApiExceptionDTO;
import com.livrotech.entity.ApiException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<ApiExceptionDTO> argumentException(
	        Exception ex) {

	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            "Id do cliente precisa ser do tipo Inteiro",
	            "ID"
	    );

	    return ResponseEntity
	            .badRequest()
	            .body(response);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class, NullPointerException.class, MethodArgumentNotValidException.class})
	public ResponseEntity<ApiExceptionDTO> handleDataIntegrity(
	        Exception ex) {

	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            "Campos obrigatórios não foram preenchidos",
	            "BODY"
	    );

	    return ResponseEntity
	            .badRequest()
	            .body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiExceptionDTO> handleBodyMissing(
	        HttpMessageNotReadableException ex) {

	    ApiExceptionDTO response = new ApiExceptionDTO(
	            400,
	            "Body da requisição é obrigatório",
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
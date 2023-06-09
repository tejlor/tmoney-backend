package pl.telech.tmoney.commons.controller.config;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.model.exception.NotFoundException;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.exception.ValidationException;
import pl.telech.tmoney.commons.model.shared.ApiError;
import pl.telech.tmoney.commons.utils.TUtils;
import pl.telech.tmoney.commons.utils.aop.AppLogOmit;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandlers {

	@Value("${tmoney.environment}")
	final String environment;
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException e) {
	    Map<String, String> errors = e.getBindingResult().getAllErrors().stream()
	    	.collect(Collectors.toMap(
	    			err -> ((FieldError) err).getField(), 
	    			err -> err.getDefaultMessage())
	    	);
	    
	    ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.BAD_REQUEST);
		apiError.setErrorMessage(errors.toString());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiError> handleValidationException(HttpServletRequest request, ValidationException e) {
	    Map<String, String> errors = e.getErrors().stream()
	    	.collect(Collectors.toMap(
	    			err -> ((FieldError) err).getField(), 
	    			err -> err.getDefaultMessage())
	    	);
	    
	    ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.BAD_REQUEST);
		apiError.setErrorMessage(errors.toString());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
	}
	
	@AppLogOmit
	@ExceptionHandler(TMoneyException.class)
	public ResponseEntity<ApiError> handleTMoneyException(HttpServletRequest request, TMoneyException e) {
		log.error("Error", e);	
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.BAD_REQUEST);
		apiError.setErrorMessage(e.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
	}

	@AppLogOmit
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request, NotFoundException e) {
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.NOT_FOUND);
		return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
	}

	@AppLogOmit
	@ExceptionHandler(RuntimeException.class)
	public Object handleSystemException(HttpServletRequest request, RuntimeException e) {
		log.error("Error", e);	
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setErrorMessage(TUtils.isProd(environment) ? "INTERNAL SERVER ERROR" : e.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

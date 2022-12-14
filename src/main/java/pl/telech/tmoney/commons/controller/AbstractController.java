package pl.telech.tmoney.commons.controller;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.exception.NotFoundException;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.ApiError;
import pl.telech.tmoney.commons.utils.TUtils;
import pl.telech.tmoney.commons.utils.aop.AppLogOmit;

@Slf4j
@RestController
public class AbstractController {
	
	@Value("${tmoney.environment}")
	String environment;

	protected static final String ID = "[0-9]+";
	protected static final String CODE = "[a-zA-Z0-9_\\-]+";
	
	protected <T extends AbstractDto & Comparable<T>> List<T> sort(List<T> list) {
		Collections.sort(list);
		return list;
	}
	
	/*
	 * Configuration needed for using LocalDate class in arguments of constructor methods without @DateTimeFormat annotation.
	 */
	@AppLogOmit
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalDate.parse(text, DateTimeFormatter.ISO_DATE));
			}
		});
		
		binder.registerCustomEditor(UUID.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(UUID.fromString(text));
			}
		});
	}

	@AppLogOmit
	@ExceptionHandler(TMoneyException.class)
	public ResponseEntity<ApiError> handleTMoneyException(HttpServletRequest request, TMoneyException e) {
		log.error(ExceptionUtils.getStackTrace(e));
		
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
		log.error(ExceptionUtils.getStackTrace(e));
		
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setErrorMessage(TUtils.isProd(environment) ? "INTERNAL SERVER ERROR" : e.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

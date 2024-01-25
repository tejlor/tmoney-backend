package pl.telech.tmoney.commons.model.exception;

import java.util.List;

import org.springframework.validation.ObjectError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

	final List<ObjectError> errors;
	
}

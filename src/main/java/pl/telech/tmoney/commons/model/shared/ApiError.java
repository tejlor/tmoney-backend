package pl.telech.tmoney.commons.model.shared;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/*
 * Object is returning in case of error. See AbstractController.
 */
@Getter @Setter
public class ApiError {

	HttpStatus statusCode;
	String errorMessage;
	LocalDateTime timestamp;
	String path;

	public ApiError() {
		timestamp = LocalDateTime.now();
	}
}

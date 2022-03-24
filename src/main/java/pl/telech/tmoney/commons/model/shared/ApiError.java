package pl.telech.tmoney.commons.model.shared;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/*
 * Object is returning in case of error. See AbstractController.
 */
@Getter @Setter
@FieldDefaults(level = PRIVATE)
public class ApiError {

	HttpStatus statusCode;
	String errorMessage;
	LocalDateTime timestamp;
	String path;

	public ApiError() {
		timestamp = LocalDateTime.now();
	}
}

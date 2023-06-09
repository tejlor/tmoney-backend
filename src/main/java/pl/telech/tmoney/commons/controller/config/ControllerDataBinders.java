package pl.telech.tmoney.commons.controller.config;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pl.telech.tmoney.commons.utils.aop.AppLogOmit;


@RestControllerAdvice
public class ControllerDataBinders {

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
}

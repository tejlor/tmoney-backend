package pl.telech.tmoney.commons.logic.validator;

import org.springframework.validation.Errors;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;


public interface DomainValidator<E extends AbstractEntity> {

	void validate(E object, Errors errors);
}

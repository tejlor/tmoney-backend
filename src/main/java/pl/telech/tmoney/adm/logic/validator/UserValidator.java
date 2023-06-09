package pl.telech.tmoney.adm.logic.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;

@Component
public class UserValidator implements DomainValidator<User> {

	@Override
	public void validate(User object, Errors errors) {

	}

}

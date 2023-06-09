package pl.telech.tmoney.adm.logic.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;

@Component
public class SettingValidator implements DomainValidator<Setting> {

	@Override
	public void validate(Setting object, Errors errors) {

	}

}

package pl.telech.tmoney.bank.logic.validator;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;

@Component
@RequiredArgsConstructor
public class AccountValidator implements DomainValidator<Account> {

	final ValidationDataProvider dataProvider;
	
	
	@CacheEvict(cacheNames = ValidationDataProvider.OTHER_ACCOUNTS, allEntries = true)
	public void validate(Account account, Errors errors) {
		activeMustHaveAllFieldsFilled(account, errors);
		codeMustBeUniqe(account, errors);
		nameMustBeUniqe(account, errors);
		orderNoMustBeUniqe(account, errors);
	}
	
	private void activeMustHaveAllFieldsFilled(Account account, Errors errors) {
		if (!account.isActive()) 
			return;
		
		if (StringUtils.isBlank(account.getColor())) {
			errors.rejectValue("color", null, "Kolor powinien być wypełniony dla aktywnego konta");
		}
		if (StringUtils.isBlank(account.getLightColor())) {
			errors.rejectValue("lightColor", null, "Kolor jasny powinien być wypełniony dla aktywnego konta");
		}
		if (StringUtils.isBlank(account.getDarkColor())) {
			errors.rejectValue("darkColor", null, "Kolor ciemny powinien być wypełniony dla aktywnego konta");
		}
		if (StringUtils.isBlank(account.getOrderNo())) {
			errors.rejectValue("orderNo", null, "Pozycja powinna być wypełniona dla aktywnego konta");
		}
		if (StringUtils.isBlank(account.getIcon())) {
			errors.rejectValue("icon", null, "Ikona powinna być wypełniona dla aktywnego konta");
		}
		if (StringUtils.isBlank(account.getLogo())) {
			errors.rejectValue("logo", null, "Logo powinno być wypełnione dla aktywnego konta");
		}
	}
	
	private void codeMustBeUniqe(Account account, Errors errors) {
		boolean isDuplicate = dataProvider.getOtherAccounts(account).stream()
			.anyMatch(acc -> Objects.equals(acc.getCode(), account.getCode()));
		
		if (isDuplicate) {
			errors.rejectValue("code", null, "Kod powinien być unikalny dla wszystkich kont");
		}
	}
	
	private void nameMustBeUniqe(Account account, Errors errors) {
		if (!account.isActive())
			return;
		
		boolean isDuplicate = dataProvider.getOtherAccounts(account).stream()
				.filter(acc -> acc.isActive())
				.anyMatch(acc -> Objects.equals(acc.getName(), account.getName()));
		
		if (isDuplicate) {
			errors.rejectValue("name", null, "Nazwa powinna być unikalna dla aktywnych kont");
		}
	}
	
	private void orderNoMustBeUniqe(Account account, Errors errors) {
		if (!account.isActive())
			return;
		
		boolean isDuplicate = dataProvider.getOtherAccounts(account).stream()
				.filter(acc -> acc.isActive())
				.anyMatch(acc -> Objects.equals(acc.getOrderNo(), account.getOrderNo()));
		
		if (isDuplicate) {
			errors.rejectValue("name", null, "Pozycja powinna być unikalna dla aktywnych kont");
		}
	}

}

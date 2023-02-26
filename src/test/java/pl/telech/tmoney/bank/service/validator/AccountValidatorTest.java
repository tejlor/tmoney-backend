package pl.telech.tmoney.bank.service.validator;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.logic.validator.AccountValidator;
import pl.telech.tmoney.bank.logic.validator.ValidationDataProvider;
import pl.telech.tmoney.bank.model.entity.Account;

@ExtendWith(SpringExtension.class)
@Import({AccountValidator.class})
class AccountValidatorTest {
	
	@Autowired
	AccountValidator validator;
	
	@MockBean
	ValidationDataProvider dataProvider;
	
	@ParameterizedTest
	@MethodSource
	void validate(Account account, String errorMessage) {
		// given
		List<Account> accounts = List.of(
				account("Konto bankowe", "BANK", true, "1.1"),
				account("Konto maklerskie", "MAKLER", true, "2.1"),
				account("Dom", "HOME", false, "2.2")
		);
		
		when(dataProvider.getOtherAccounts(account)).thenReturn(accounts);
		
		// when
		Errors errors = new BeanPropertyBindingResult(account, "account");
		validator.validate(account, errors);
		
		// then
		assertThat(errors.hasErrors()).isEqualTo(errorMessage != null);
		
		if (errorMessage != null) {
			assertThat(errors.getAllErrors().get(0).getDefaultMessage()).isEqualTo(errorMessage);
		}
	}
	
	private Stream<Arguments> validate() {
		return Stream.of(
				Arguments.of(new AccountBuilder().color(null).build(), "Kolor powinien być wypełniony dla aktywnego konta"),
				Arguments.of(new AccountBuilder().lightColor(null).build(), "Kolor jasny powinien być wypełniony dla aktywnego konta"),
				Arguments.of(new AccountBuilder().darkColor(null).build(), "Kolor ciemny powinien być wypełniony dla aktywnego konta"),
				Arguments.of(new AccountBuilder().orderNo(null).build(), "Pozycja powinna być wypełniona dla aktywnego konta"),
				Arguments.of(new AccountBuilder().icon(null).build(), "Ikona powinna być wypełniona dla aktywnego konta"),
				Arguments.of(new AccountBuilder().logo(null).build(), "Logo powinno być wypełnione dla aktywnego konta"),
				Arguments.of(account("Dom2", "HOME", true, "9.9"), "Kod powinien być unikalny dla wszystkich kont"),
				Arguments.of(account("Konto bankowe", "BANK_B", true, "9.9"), "Nazwa powinna być unikalna dla aktywnych kont"),
				Arguments.of(account("Konto bankowe 2", "BANK_B", true, "2.1"), "Pozycja powinna być unikalna dla aktywnych kont"),
				Arguments.of(account("Konto bankowe 2", "BANK_B", true, "2.2"), null)
		);
	}
	
	public Account account(String name, String code, boolean active, String orderNo) {
		return new AccountBuilder()
			.name(name)
			.code(code)
			.active(active)
			.orderNo(orderNo)
			.build();
	}
	
}

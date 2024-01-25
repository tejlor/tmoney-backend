package pl.telech.tmoney.bank.service.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
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
import pl.telech.tmoney.bank.builder.TransferDefinitionBuilder;
import pl.telech.tmoney.bank.logic.validator.TransferDefinitionValidator;
import pl.telech.tmoney.bank.logic.validator.ValidationDataProvider;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;

@ExtendWith(SpringExtension.class)
@Import({TransferDefinitionValidator.class})
class TransferDefinitionValidatorTest {
	
	@Autowired
	TransferDefinitionValidator validator;
	
	@MockBean
	ValidationDataProvider dataProvider;
	
	Account bank, home;
	
	@BeforeAll
	void init() {
		bank = account(1, "Bank");
		home = account(2, "Dom");
	}
	
	@ParameterizedTest
	@MethodSource
	void validate(TransferDefinition definition, String errorMessage) {
		// given
		List<TransferDefinition> definitions = List.of(
				definition(bank, home)
		);
		
		when(dataProvider.getOtherTransferDefinitions(definition)).thenReturn(definitions);
		
		// when
		Errors errors = new BeanPropertyBindingResult(definition, "definition");
		validator.validate(definition, errors);
		
		// then
		assertThat(errors.hasErrors()).isEqualTo(errorMessage != null);
		
		if (errorMessage != null) {
			assertThat(errors.getAllErrors().get(0).getDefaultMessage()).isEqualTo(errorMessage);
		}
	}
	
	private Stream<Arguments> validate() {
		return Stream.of(
				Arguments.of(definition(bank, bank), "Konto docelowe musi być inne niż źródłowe"),
				Arguments.of(definition(bank, home), "Istnieje już definicja o takiej samej parze kont"),
				Arguments.of(definition(home, bank), null)
		);
	}
	
	private TransferDefinition definition(Account source, Account destination) {
		return new TransferDefinitionBuilder()
				.sourceAccount(source)
				.destinationAccount(destination)
				.build();
	}
	
	private Account account(int id, String name) {
		return new AccountBuilder()
				.name(name)
				.id(id)
				.build();
	}
	
}

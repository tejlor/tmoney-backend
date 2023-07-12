package pl.telech.tmoney.bank.service.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.telech.tmoney.utils.TestUtils.date;

import java.time.LocalDate;
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
import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.logic.validator.EntryValidator;
import pl.telech.tmoney.bank.logic.validator.ValidationDataProvider;
import pl.telech.tmoney.bank.model.entity.Entry;

@ExtendWith(SpringExtension.class)
@Import({EntryValidator.class})
class EntryValidatorTest {
	
	@Autowired
	EntryValidator validator;
	
	@MockBean
	ValidationDataProvider dataProvider;
	
	
	@ParameterizedTest
	@MethodSource
	void validate(Entry entry, String errorMessage) {
		// given
		List<Entry> entries = List.of(
				entry("Zakupy", date("2022-05-12")),
				entry("Myjnia", date("2022-05-12"))
		);
		
		when(dataProvider.getOtherAccountEntries(any())).thenReturn(entries);
		
		// when
		Errors errors = new BeanPropertyBindingResult(entry, "entry");
		validator.validate(entry, errors);
		
		// then
		assertThat(errors.hasErrors()).isEqualTo(errorMessage != null);
		
		if (errorMessage != null) {
			assertThat(errors.getAllErrors().get(0).getDefaultMessage()).isEqualTo(errorMessage);
		}
	}
	
	private Stream<Arguments> validate() {
		return Stream.of(
				Arguments.of(entry("Zakupy", date("2022-05-12")), "Nazwa powinna być unikalna dla tego samego dnia"),
				Arguments.of(entry("Myjnia", date("2022-05-13")), null),
				Arguments.of(entry("Zakupy Lidl", date("2022-05-12")), null)
		);
	}
	
	public Entry entry(String name, LocalDate date) {
		return new EntryBuilder()
			.name(name)
			.account(new AccountBuilder().id(1).build())
			.date(date)
			.build();
	}
	
}

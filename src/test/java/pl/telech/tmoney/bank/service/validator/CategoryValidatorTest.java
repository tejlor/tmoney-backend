package pl.telech.tmoney.bank.service.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

import pl.telech.tmoney.bank.builder.CategoryBuilder;
import pl.telech.tmoney.bank.logic.validator.CategoryValidator;
import pl.telech.tmoney.bank.logic.validator.ValidationDataProvider;
import pl.telech.tmoney.bank.model.entity.Category;

@ExtendWith(SpringExtension.class)
@Import({CategoryValidator.class})
class CategoryValidatorTest {
	
	@Autowired
	CategoryValidator validator;
	
	@MockBean
	ValidationDataProvider dataProvider;
	
	
	@ParameterizedTest
	@MethodSource
	void validate(Category category, String errorMessage) {
		// given
		List<Category> categories = List.of(
				category("Zakupy spożywcze"),
				category("Samochód")
		);
		
		when(dataProvider.getOtherCategories(category)).thenReturn(categories);
		
		// when
		Errors errors = new BeanPropertyBindingResult(category, "category");
		validator.validate(category, errors);
		
		// then
		assertThat(errors.hasErrors()).isEqualTo(errorMessage != null);
		
		if (errorMessage != null) {
			assertThat(errors.getAllErrors().get(0).getDefaultMessage()).isEqualTo(errorMessage);
		}
	}
	
	private Stream<Arguments> validate() {
		return Stream.of(
				Arguments.of(category("Samochód"), "Nazwa powinna być unikalna dla wszystkich kategorii"),
				Arguments.of(category("Wypłata"), null)
		);
	}
	
	public Category category(String name) {
		return new CategoryBuilder()
			.name(name)
			.build();
	}
	
}

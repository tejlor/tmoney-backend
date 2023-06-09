package pl.telech.tmoney.bank.logic.validator;

import java.util.Objects;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;

@Component
@RequiredArgsConstructor
public class CategoryValidator implements DomainValidator<Category> {

	final ValidationDataProvider dataProvider;
	
	
	@CacheEvict(cacheNames = ValidationDataProvider.OTHER_CATEGORIES, allEntries = true)
	public void validate(Category category, Errors errors) {
		nameMustBeUniqe(category, errors);
	}	
	
	private void nameMustBeUniqe(Category category, Errors errors) {
		boolean isDuplicate = dataProvider.getOtherCategories(category).stream()
				.anyMatch(cat -> Objects.equals(cat.getName(), category.getName()));
		
		if (isDuplicate) {
			errors.rejectValue("name", null, "Nazwa powinna być unikalna dla wszystkich kategorii");
		}
	}

}

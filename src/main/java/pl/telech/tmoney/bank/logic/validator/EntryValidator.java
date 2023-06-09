package pl.telech.tmoney.bank.logic.validator;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;

@Component
@RequiredArgsConstructor
public class EntryValidator implements DomainValidator<Entry> {

	final ValidationDataProvider dataProvider;
	
	
	@CacheEvict(cacheNames = ValidationDataProvider.OTHER_ACCOUNT_ENTRIES, allEntries = true)
	public void validate(Entry entry, Errors errors) {
		nameAndDateMustBeUniqe(entry, errors);
	}
	
	private void nameAndDateMustBeUniqe(Entry entry, Errors errors) {
		boolean isDuplicate = dataProvider.getOtherAccountEntries(entry).stream()
				.anyMatch(ent -> ent.getName().equals(entry.getName()) && ent.getDate().equals(entry.getDate()));
		
		if (isDuplicate) {
			errors.rejectValue("name", null, "Nazwa powinna być unikalna dla tego samego dnia");
		}
	}

}

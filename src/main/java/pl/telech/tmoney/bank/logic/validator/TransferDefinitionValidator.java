package pl.telech.tmoney.bank.logic.validator;

import java.util.Objects;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;

@Component
@RequiredArgsConstructor
public class TransferDefinitionValidator implements DomainValidator<TransferDefinition> {
 
	final ValidationDataProvider dataProvider;
	
	
	@CacheEvict(cacheNames = ValidationDataProvider.OTHER_TRANSFER_DEFINITIONS, allEntries = true)
	public void validate(TransferDefinition definition, Errors errors) {
		accountsMustBeDifferent(definition, errors);
		accountsPairMustBeUniqe(definition, errors);
	}
	
	private void accountsMustBeDifferent(TransferDefinition definition, Errors errors) {
		if (Objects.equals(definition.getSourceAccount(), definition.getDestinationAccount())) {
			errors.rejectValue("destinationAccount", null, "Konto docelowe musi być inne niż źródłowe");
		}
	}
	
	private void accountsPairMustBeUniqe(TransferDefinition definition, Errors errors) {
		boolean isDuplicate = dataProvider.getOtherTransferDefinitions(definition).stream()
			.anyMatch(def -> Objects.equals(def.getSourceAccount(), definition.getSourceAccount()) 
					&& Objects.equals(def.getDestinationAccount(), definition.getDestinationAccount()));
		
		if (isDuplicate) {
			errors.rejectValue("sourceAccount", null, "Istnieje już definicja o takiej samej parze kont");
		}
	}

}

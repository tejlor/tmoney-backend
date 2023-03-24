package pl.telech.tmoney.bank.asserts;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.asserts.EntityAssert;


public class TransferDefinitionAssert extends EntityAssert<TransferDefinition, TransferDefinitionDto> {

	private TransferDefinitionAssert(TransferDefinitionDto result) {
		super(result);
		
		addCondition("sourceAccount", Pair.of(entity -> entity.getSourceAccount().getId(), dto -> dto.getSourceAccount().getId()));	
		addCondition("destinationAccount", Pair.of(entity -> entity.getDestinationAccount().getId(), dto -> dto.getDestinationAccount().getId()));			
		addCondition("category", Pair.of(entity -> entity.getCategory().getId(), dto -> dto.getCategory().getId()));	
		addCondition("name", Pair.of(TransferDefinition::getName, TransferDefinitionDto::getName));	
		addCondition("description", Pair.of(TransferDefinition::getDescription, TransferDefinitionDto::getDescription));	
	}
	
	public static TransferDefinitionAssert assertThatDto(TransferDefinitionDto result) {
		return new TransferDefinitionAssert(result);
	}
		
}

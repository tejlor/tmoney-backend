package pl.telech.tmoney.bank.helper;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.builder.TransferDefinitionBuilder;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;

@Component
public class TransferDefinitionHelper {

	@Autowired
	EntityManager entityManager;
	
	
	@Transactional
	public TransferDefinition save(String name) {
		return new TransferDefinitionBuilder()
			.name(name)
			.save(entityManager);
	}
	
}

package pl.telech.tmoney.bank.builder;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@Accessors(chain = true, fluent = true)
public class TransferDefinitionBuilder extends AbstractBuilder<TransferDefinition> {
	
	Account sourceAccount = new AccountBuilder().build();		
	Account destinationAccount = new AccountBuilder().build();					
	Category category = new CategoryBuilder().build();			
	String name = "Zakup telewizora";						
	String description = "sklep Morele";				
	
	@Override
	public TransferDefinition build() {
		var obj = new TransferDefinition();
		super.fill(obj);
		obj.setSourceAccount(sourceAccount);
		obj.setDestinationAccount(destinationAccount);
		obj.setCategory(category);
		obj.setName(name);
		obj.setDescription(description);
		return obj;	
	}

	@Override
	protected void persistDependencies(EntityManager em) {
		if (sourceAccount != null && sourceAccount.getId() == null) {
			em.persist(sourceAccount);
		}
		if (destinationAccount != null && destinationAccount.getId() == null) {
			em.persist(destinationAccount);
		}
		if (category != null && category.getId() == null) {
			em.persist(category);
		}
	}
}

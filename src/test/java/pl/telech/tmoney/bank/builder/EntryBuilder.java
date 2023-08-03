package pl.telech.tmoney.bank.builder;

import static pl.telech.tmoney.utils.TestUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@Accessors(chain = true, fluent = true)
public class EntryBuilder extends AbstractBuilder<Entry> {
	
	Account account = new AccountBuilder().build();			
	LocalDate date = LocalDate.now();				
	Category category = new CategoryBuilder().build();			
	String name = "Zakup telewizora";				
	BigDecimal amount = dec("10.00");			
	String description = "sklep Morele";			
	BigDecimal balance = dec("1.00");			
	BigDecimal balanceOverall = dec("10.00");	
	String externalId = null;
	
	@Override
	public Entry build() {
		var obj = new Entry();
		super.fill(obj);
		obj.setAccount(account);
		obj.setDate(date);
		obj.setCategory(category);
		obj.setName(name);
		obj.setAmount(amount);
		obj.setDescription(description);
		obj.setBalance(balance);
		obj.setBalanceOverall(balanceOverall);
		obj.setExternalId(externalId);
		return obj;	
	}

	@Override
	protected void persistDependencies(EntityManager em) {
		if (account != null && account.getId() == null) {
			em.persist(account);
		}
		if (category != null && category.getId() == null) {
			em.persist(category);
		}
	}
}

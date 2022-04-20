package pl.telech.tmoney.bank.builder;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class EntryBuilder extends AbstractBuilder<Entry> {
	
	Account account = new AccountBuilder().build();			
	LocalDate date = LocalDate.now();				
	Category category = new CategoryBuilder().build();			
	String name = "Zakup telewizora";				
	BigDecimal amount = BigDecimal.TEN;			
	String description = "w sklepie Morele";			
	BigDecimal balance = BigDecimal.ONE;			
	BigDecimal balanceOverall = BigDecimal.TEN;	
	
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

		return obj;	
	}

	@Override
	public void persistDependences(EntityManager em) {
		if(account != null) {
			em.persist(account);
		}
		if(category != null) {
			em.persist(category);
		}
	}
}

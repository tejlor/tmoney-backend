package pl.telech.tmoney.bank.builder;

import static pl.telech.tmoney.utils.TestUtils.dec;

import java.math.BigDecimal;
import java.util.List;

import lombok.Setter;
import lombok.experimental.Accessors;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@Accessors(chain = true, fluent = true)
public class CategoryBuilder extends AbstractBuilder<Category> {
	
	String name = "Zakupy";				
	List<Account> accounts = List.of();			
	Boolean report = true;				
	String defaultName = "Zakupy spożywcze";			
	BigDecimal defaultAmount = dec("-1.00");	
	String defaultDescription = "opis";	
	
	@Override
	public Category build() {
		var obj = new Category();
		super.fill(obj);
		obj.setName(name);
		obj.setAccounts(accounts);
		obj.setReport(report);
		obj.setDefaultName(defaultName);
		obj.setDefaultAmount(defaultAmount);
		obj.setDefaultDescription(defaultDescription);
		return obj;	
	}
}

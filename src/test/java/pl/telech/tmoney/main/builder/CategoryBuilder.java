package pl.telech.tmoney.main.builder;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.main.model.entity.Category;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class CategoryBuilder extends AbstractBuilder<Category> {
	
	String name = "Zakupy";				
	Integer account = 1;			
	Boolean report = true;				
	String defaultName = "Zakupy spożywcze";			
	BigDecimal defaultAmount = BigDecimal.ONE.negate();	
	String defaultDescription = "opis";	
	
	@Override
	public Category build() {
		var obj = new Category();
		super.fill(obj);
		obj.setName(name);
		obj.setAccount(account);
		obj.setReport(report);
		obj.setDefaultName(defaultName);
		obj.setDefaultAmount(defaultAmount);
		obj.setDefaultDescription(defaultDescription);
		return obj;	
	}
}

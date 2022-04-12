package pl.telech.tmoney.main.builder;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.main.model.entity.Account;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class AccountBuilder extends AbstractBuilder<Account> {
	
	String code = "HOME";
	String name = "Dom";				
	Boolean active = true;			
	String color = "255.214,102";				
	Integer orderNo = 1;	
	
	@Override
	public Account build() {
		var obj = new Account();
		super.fill(obj);
		obj.setCode(code);
		obj.setName(name);
		obj.setActive(active);
		obj.setColor(color);
		obj.setOrderNo(orderNo);
		return obj;	
	}

	@Override
	public void persistDependences(EntityManager em) {

	}
}

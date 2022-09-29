package pl.telech.tmoney.bank.builder;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class AccountBuilder extends AbstractBuilder<Account> {
	
	String code = "HOME";
	String name = "Dom";				
	Boolean active = true;			
	String color = "45ffc9";		
	String lightColor = "45ffcc";		
	String darkColor = "45ffcf";		
	String orderNo = "1.1";	
	String icon = "fa-user";
	
	@Override
	public Account build() {
		var obj = new Account();
		super.fill(obj);
		obj.setCode(code);
		obj.setName(name);
		obj.setActive(active);
		obj.setColor(color);
		obj.setLightColor(lightColor);
		obj.setDarkColor(darkColor);
		obj.setOrderNo(orderNo);
		obj.setIcon(icon);
		return obj;	
	}

	@Override
	public void persistDependences(EntityManager em) {

	}
}

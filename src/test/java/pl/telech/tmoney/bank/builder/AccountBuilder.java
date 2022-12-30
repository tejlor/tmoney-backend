package pl.telech.tmoney.bank.builder;

import lombok.Setter;
import lombok.experimental.Accessors;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@Accessors(chain = true, fluent = true)
public class AccountBuilder extends AbstractBuilder<Account> {
	
	String name = "Dom";
	String code = "HOME";
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
		obj.setName(name);
		obj.setCode(code);
		obj.setActive(active);
		obj.setColor(color);
		obj.setLightColor(lightColor);
		obj.setDarkColor(darkColor);
		obj.setOrderNo(orderNo);
		obj.setIcon(icon);
		return obj;	
	}
}

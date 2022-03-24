package pl.telech.tmoney.adm.builder;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.utils.TUtils;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class UserBuilder extends AbstractBuilder<User> {
	
	public static final String DEFAULT_PASSWORD = "password";
	
	String firstName = "Adam";				
	String lastName = "Nowakowski";				
	String email = "adam.nowakowski@gmail.com";					
	String password = TUtils.sha1(DEFAULT_PASSWORD);	
	
	@Override
	public User build() {
		var obj = new User();
		super.fill(obj);
		obj.setFirstName(firstName);
		obj.setLastName(lastName);
		obj.setEmail(email);
		obj.setPassword(password);
		return obj;	
	}
}

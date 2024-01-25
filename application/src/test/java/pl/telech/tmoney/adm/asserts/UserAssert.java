package pl.telech.tmoney.adm.asserts;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.asserts.EntityAssert;

public class UserAssert extends EntityAssert<User, UserDto> {

	private UserAssert(UserDto result) {
		super(result);
		
		addCondition("firstName", 	Pair.of(User::getFirstName, UserDto::getFirstName));
		addCondition("lastName", 	Pair.of(User::getLastName, UserDto::getLastName));
		addCondition("email", 		Pair.of(User::getEmail, UserDto::getEmail));

	}
	
	public static UserAssert assertThatDto(UserDto result) {
		return new UserAssert(result);
	}
		
}

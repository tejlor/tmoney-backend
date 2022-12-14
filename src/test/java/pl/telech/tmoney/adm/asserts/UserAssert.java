package pl.telech.tmoney.adm.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;

public class UserAssert extends AbstractAssert<UserAssert, UserDto> {

	private UserAssert(UserDto actual) {
		super(actual, UserAssert.class);
	}
	
	public static UserAssert assertThat(UserDto actual) {
		return new UserAssert(actual);
	}
	
	public UserAssert isEqualTo(User entity) {
		var asserts = new SoftAssertions();
		asserts.assertThat(actual.getId()).isEqualTo(entity.getId());
		asserts.assertThat(actual.getFirstName()).isEqualTo(entity.getFirstName());
		asserts.assertThat(actual.getLastName()).isEqualTo(entity.getLastName());
		asserts.assertThat(actual.getEmail()).isEqualTo(entity.getEmail());
		asserts.assertAll();
		return this;
	}
		
}

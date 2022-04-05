package pl.telech.tmoney.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class UserControllerTest extends BaseTest {

	@Autowired
	UserController userController;
	
	@Test
	@Transactional
	public void getCurrentUser() {	
		// given
		flush();
		// when
		UserDto result = userController.getCurrentUser();	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertCurrentUser(result, defaultUser);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertCurrentUser(UserDto userDto, User user) {
		assertThat(userDto.getId()).isEqualTo(user.getId());
		assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
		assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
	}
	
}

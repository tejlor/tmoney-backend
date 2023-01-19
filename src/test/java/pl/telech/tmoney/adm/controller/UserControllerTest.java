package pl.telech.tmoney.adm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.telech.tmoney.adm.UserHelper;
import pl.telech.tmoney.adm.asserts.UserAssert;
import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.mock.SecurityContextMock;
import pl.telech.tmoney.utils.BaseMvcTest;


class UserControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/users";
	
	@Autowired
	UserHelper userHelper;
	
	
	@Test
	void getCurrentUser() throws Exception {
		// given
		User user = userHelper.save("user@myapp.com");
		
		SecurityContextHolder.setContext(SecurityContextMock.withUser(user));
		
		// when
		UserDto responseDto = get(baseUrl + "/current", UserDto.class);
		
		// then	
		UserAssert.assertThatDto(responseDto)
			.isMappedFrom(user);
	}
	
}

package pl.telech.tmoney.adm.controller.domain;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.adm.logic.UserLogic;
import pl.telech.tmoney.adm.mapper.UserMapper;
import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends AbstractDomainController<User, UserDto> {

	final UserMapper mapper;
	final UserLogic userLogic;
	
	/*
	 * Returns current user.
	 */
	@RequestMapping(value = "/current", method = GET)
	public UserDto getCurrentUser() {
		
		return mapper.toDto(userLogic.loadCurrentUser());
	}
}

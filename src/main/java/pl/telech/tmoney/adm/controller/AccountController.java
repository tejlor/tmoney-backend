package pl.telech.tmoney.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.logic.AccountLogicImpl;
import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.commons.controller.AbstractController;

@RestController
@RequestMapping("/accounts")
@FieldDefaults(level = PRIVATE)
public class AccountController extends AbstractController {

	@Autowired
	AccountLogicImpl accountLogic;
	
	/*
	 * Returns current user.
	 */
	@RequestMapping(value = "/current", method = GET)
	public UserDto getCurrentUser() {
		
		return new UserDto(accountLogic.loadCurrentUser());
	}
}

package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.main.logic.interfaces.AccountLogic;
import pl.telech.tmoney.main.model.dto.AccountDto;

@RestController
@RequestMapping("/bank-accounts")
@FieldDefaults(level = PRIVATE)
public class AccountController extends AbstractController {

	@Autowired
	AccountLogic accountLogic;
	
	/*
	 * Returns account by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public AccountDto getById(int id) {
		
		return new AccountDto(accountLogic.loadById(id));
	}
}

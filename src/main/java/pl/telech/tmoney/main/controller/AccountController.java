package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.utils.TUtils;
import pl.telech.tmoney.main.logic.interfaces.AccountLogic;
import pl.telech.tmoney.main.model.dto.AccountDto;

@RestController
@RequestMapping("/bank-accounts")
@FieldDefaults(level = PRIVATE)
public class AccountController extends AbstractController {

	@Autowired
	AccountLogic accountLogic;
	
	/*
	 * Returns all accounts.
	 */
	@RequestMapping(value = "", method = GET)
	public List<AccountDto> getAll() {
		
		return AccountDto.toDtoList(accountLogic.loadAll());
	}
	
	/*
	 * Returns account by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public AccountDto getById(int id) {
		
		return new AccountDto(accountLogic.loadById(id));
	}
	
	/*
	 * Creates new account.
	 */
	@RequestMapping(value = "", method = POST)
	public AccountDto create(
			@RequestBody AccountDto account) {
		
		return new AccountDto(accountLogic.create(account.toModel()));
	}
	
	/*
	 * Updates account.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public AccountDto update(
			@PathVariable int id,
			@RequestBody AccountDto account) {
		
		TUtils.assertDtoId(id, account);
		return new AccountDto(accountLogic.update(id, account.toModel()));
	}
}

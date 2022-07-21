package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.dto.AccountWithEntryDto;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequestMapping("/bank-accounts")
@FieldDefaults(level = PRIVATE)
public class AccountController extends AbstractController {

	@Autowired
	AccountLogic accountLogic;
	
	/*
	 * Returns active accounts.
	 */
	@RequestMapping(value = "", method = GET)
	public List<AccountDto> getActive() {
		
		return AccountDto.toDtoList(accountLogic.loadActive());
	}
	
	/*
	 * Returns active accounts with last entries.
	 */
	@RequestMapping(value = "/summary", method = GET)
	public List<AccountWithEntryDto> getSummary() {	
		return accountLogic.getAccountSummaryList().stream()
			.map(pair -> new AccountWithEntryDto(
					new AccountDto(pair.getKey()), 
					new EntryDto(pair.getValue())))
			.collect(Collectors.toList());
	}
	
	/*
	 * Returns account by id.
	 */
	@RequestMapping(value = "/id/{id:" + ID + "}", method = GET)
	public AccountDto getById(int id) {
		
		return new AccountDto(accountLogic.loadById(id));
	}
	
	/*
	 * Returns account by code.
	 */
	@RequestMapping(value = "/{code:" + CODE + "}", method = GET)
	public AccountDto getByCode(
			@PathVariable String code) {
		
		return new AccountDto(accountLogic.loadByCode(code));
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

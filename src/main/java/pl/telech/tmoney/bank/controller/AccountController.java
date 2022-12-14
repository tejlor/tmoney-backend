package pl.telech.tmoney.bank.controller;

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

import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.dto.AccountSummaryDto;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequestMapping("/bank-accounts")
public class AccountController extends AbstractController {

	@Autowired
	AccountMapper mapper;
	@Autowired
	EntryMapper entryMapper;
	
	@Autowired
	AccountLogic accountLogic;
	
	/*
	 * Returns active accounts.
	 */
	@RequestMapping(value = "", method = GET)
	public List<AccountDto> getActive() {
		
		return mapper.toDtoList(accountLogic.loadActive());
	}
	
	/*
	 * Returns account with last entry.
	 */
	@RequestMapping(value = {"/summary", "/summary/{code:" + CODE + "}"}, method = GET)
	public List<AccountSummaryDto> getSummary(
			@PathVariable(required = false) String code) {
		
		return accountLogic.getAccountSummaries(code).stream()
				.map(pair -> new AccountSummaryDto(
					mapper.toDto(pair.getKey()), 
					entryMapper.toDto(pair.getValue()))
				)
				.collect(Collectors.toList());
	}
	
	/*
	 * Returns account by id.
	 */
	@RequestMapping(value = "/id/{id:" + ID + "}", method = GET)
	public AccountDto getById(int id) {
		
		return mapper.toDto(accountLogic.loadById(id));
	}
	
	/*
	 * Returns account by code.
	 */
	@RequestMapping(value = "/{code:" + CODE + "}", method = GET)
	public AccountDto getByCode(
			@PathVariable String code) {
		
		return mapper.toDto(accountLogic.loadByCode(code));
	}
	
	/*
	 * Creates new account.
	 */
	@RequestMapping(value = "", method = POST)
	public AccountDto create(
			@RequestBody AccountDto account) {
		
		return mapper.toDto(accountLogic.create(account));
	}
	
	/*
	 * Updates account.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public AccountDto update(
			@PathVariable int id,
			@RequestBody AccountDto account) {
		
		TUtils.assertDtoId(id, account);
		return mapper.toDto(accountLogic.update(id, account));
	}
}

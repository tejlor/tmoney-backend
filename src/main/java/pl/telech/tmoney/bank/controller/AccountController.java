package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.logic.AccountLogic;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.dto.AccountSummaryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.dto.TableDataDto;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank-accounts")
public class AccountController extends AbstractController {

	final AccountMapper mapper;
	final EntryMapper entryMapper;
	final AccountLogic accountLogic;
	
	
	/*
	 * Returns account by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public AccountDto getById(
			@PathVariable int id) {
		
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
	 * Returns all accounts.
	 */
	@RequestMapping(value = "", method = GET)
	public List<AccountDto> getAll(
			@RequestParam(defaultValue = "false") boolean active) {
		
		return mapper.toDtoList(accountLogic.loadAll(active));
	}
	
	/*
	 * Returns all accounts for table.
	 */
	@RequestMapping(value = "/table", method = GET)
	public TableDataDto<AccountDto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy) {
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<Account>, Integer> result = accountLogic.loadTable(tableParams); 	
		var table = new TableDataDto<AccountDto>(tableParams);
		table.setRows(mapper.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
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
	 * Creates new account.
	 */
	@RequestMapping(value = "", method = POST)
	public AccountDto create(
			@RequestBody @Valid AccountDto account) {
		
		return mapper.toDto(accountLogic.create(account));
	}
	
	/*
	 * Updates account.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public AccountDto update(
			@PathVariable int id,
			@RequestBody @Valid AccountDto account) {
		
		TUtils.assertDtoId(id, account);
		return mapper.toDto(accountLogic.update(id, account));
	}
}

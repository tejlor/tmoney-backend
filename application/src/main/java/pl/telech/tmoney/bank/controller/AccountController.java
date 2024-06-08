package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.telech.processor.annotation.Argument;
import pl.telech.processor.annotation.AutoMethod;
import pl.telech.processor.annotation.enums.Type;
import pl.telech.tmoney.bank.logic.AccountLogic;
import pl.telech.tmoney.bank.logic.BankLogic;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.data.AccountSummaryData;
import pl.telech.tmoney.bank.model.data.BalanceRequest;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.commons.controller.AbstractController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank-accounts")
public class AccountController extends AbstractController {

	final AccountMapper mapper;
	final EntryMapper entryMapper;
	final AccountLogic accountLogic;
	final BankLogic bankLogic;
	
	
	@AutoMethod(type = Type.GET_BY_ID)
	@AutoMethod(type = Type.GET_BY_CODE)
	@AutoMethod(type = Type.GET_ALL, args = {@Argument(name = "active", type = "boolean", defaultValue = "false")})
	@AutoMethod(type = Type.GET_TABLE)
	@AutoMethod(type = Type.CREATE)
	@AutoMethod(type = Type.UPDATE)
	private void init() {}
	
	
	/*
	 * Returns account with last entry.
	 */
	@RequestMapping(value = {"/summary", "/summary/{code:" + CODE + "}"}, method = GET)
	public List<AccountSummaryData> getSummary(
			@PathVariable(required = false) String code) {
		
		return accountLogic.getAccountSummaries(code).stream()
				.map(pair -> new AccountSummaryData(
					mapper.toDto(pair.getKey()), 
					entryMapper.toDto(pair.getValue()))
				)
				.collect(Collectors.toList());
	}
	
	/*
	 * Balance the account.
	 */
	@RequestMapping(value = "/{id:" + ID + "}/balance", method = POST)
	public EntryDto balance(
			@PathVariable int id,
			@RequestBody @Valid BalanceRequest request) {
		
		return entryMapper.toDto(bankLogic.balanceAccount(request));
	}
}

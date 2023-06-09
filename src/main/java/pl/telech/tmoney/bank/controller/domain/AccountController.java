package pl.telech.tmoney.bank.controller.domain;

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
import pl.telech.tmoney.bank.logic.AccountLogic;
import pl.telech.tmoney.bank.logic.BankLogic;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.data.AccountSummaryData;
import pl.telech.tmoney.bank.model.data.BalanceRequest;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank-accounts")
public class AccountController extends AbstractDomainController<Account, AccountDto> {

	final AccountMapper mapper;
	final EntryMapper entryMapper;
	final AccountLogic accountLogic;
	final BankLogic bankLogic;
	
	
	/*
	 * Returns account by code.
	 */
	@RequestMapping(value = "/{code:" + CODE + "}", method = GET)
	public AccountDto getByCode(@PathVariable String code) {	
		return mapper.toDto(accountLogic.loadByCode(code));
	}
	
	/*
	 * Returns account with last entry.
	 */
	@RequestMapping(value = {"/summary", "/summary/{code:" + CODE + "}"}, method = GET)
	public List<AccountSummaryData> getSummary(@PathVariable(required = false) String code) {		
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
	public void balance(@PathVariable int id, @RequestBody @Valid BalanceRequest request) {
		bankLogic.balanceAccount(request);
	}
}

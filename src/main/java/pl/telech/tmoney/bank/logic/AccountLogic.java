package pl.telech.tmoney.bank.logic;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.dao.AccountDAO;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
public class AccountLogic extends AbstractLogicImpl<Account> {
	
	static final Account summaryAccount;
	
	AccountDAO dao;

	
	static {
		summaryAccount = new Account();
		summaryAccount.setActive(true);
		summaryAccount.setCode(Account.SUMMARY_CODE);
		summaryAccount.setName("Podsumowanie");
	}
	
	public AccountLogic(AccountDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public Account loadByCode(String code) {
		return dao.findByCode(code);
	}
	
	public Account getSummaryAccount() {
		return summaryAccount;
	}
	
	public List<Account> loadActive() {
		return dao.findActive();
	}
	
	public Account create(AccountDto _account) {
		var account = new Account();
		account.setCode(_account.getCode());
		account.setName(_account.getName());
		account.setActive(_account.getActive());
		account.setColor(_account.getColor());
		account.setLightColor(_account.getLightColor());
		account.setDarkColor(_account.getDarkColor());
		account.setOrderNo(_account.getOrderNo());
		
		return save(account);
	}
	
	public Account update(int id, AccountDto _account) {
		Account account = loadById(id);
		TUtils.assertEntityExists(account);
				
		account.setName(_account.getName());
		account.setActive(_account.getActive());
		account.setColor(_account.getColor());
		account.setOrderNo(_account.getOrderNo());
		
		return save(account);
	}
	
	public List<Pair<Account, Entry>> getAccountSummaries(String accountCode) {	
		List<String> accountCodes = accountCode != null
				? Collections.singletonList(accountCode)
				: dao.findActive().stream().map(Account::getCode).collect(Collectors.toList());
		
		return getAccountSummaries(accountCodes);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private List<Pair<Account, Entry>> getAccountSummaries(List<String> accountCodes) {
		return accountCodes.stream()
				.map(code -> {
					var account = dao.findByCode(code);
					Entry entry = null; //entryLogic.loadLastByAccount(account.getId());
					return Pair.of(account, entry);
				})
				.collect(Collectors.toList());
	}
}

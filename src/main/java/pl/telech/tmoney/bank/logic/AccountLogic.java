package pl.telech.tmoney.bank.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.AccountDAO;
import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogic;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountLogic extends AbstractLogic<Account> {
	
	private static final Account summaryAccount;
	
	final AccountDAO dao;
	final EntryDAO entryDao; //TODO wywalić
	final AccountMapper mapper;

	
	static {
		summaryAccount = new Account();
		summaryAccount.setActive(true);
		summaryAccount.setCode(Account.SUMMARY_CODE);
		summaryAccount.setName("Podsumowanie");
	}
	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
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
	
	public Account create(AccountDto accountDto) {
		return save(mapper.create(accountDto));
	}
	
	public Account update(int id, AccountDto accountDto) {
		Account account = loadById(id);			
		mapper.update(account, accountDto);
		return save(account);
	}
	
	public List<Pair<Account, Entry>> getAccountSummaries(String accountCode) {	
		List<String> accountCodes = accountCode != null
				? List.of(accountCode)
				: dao.findActive().stream().map(Account::getCode).collect(Collectors.toList());
		
		return getAccountSummaries(accountCodes);
	}
	
	private List<Pair<Account, Entry>> getAccountSummaries(List<String> accountCodes) {
		return accountCodes.stream()
				.map(code -> {
					var account = dao.findByCode(code);
					Entry entry = entryDao.findLastByAccountBeforeDate(account.getId(), LocalDate.now().plusDays(1));
					return Pair.of(account, entry);
				})
				.collect(Collectors.toList());
	}
}

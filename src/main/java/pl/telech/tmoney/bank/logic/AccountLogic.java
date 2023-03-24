package pl.telech.tmoney.bank.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.AccountDAO;
import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.logic.validator.AccountValidator;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogic;
import pl.telech.tmoney.commons.model.exception.ValidationException;
import pl.telech.tmoney.commons.model.shared.TableParams;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountLogic extends AbstractLogic<Account> {
	
	private static final Account summaryAccount;
	
	final AccountDAO dao;
	final EntryDAO entryDao; //TODO wywalić
	final AccountMapper mapper;
	final AccountValidator validator;

	
	static {
		summaryAccount = new Account();
		summaryAccount.setActive(true);
		summaryAccount.setCode(Account.SUMMARY_CODE);
		summaryAccount.setName("Podsumowanie");
		summaryAccount.setLogo("data:image/jpeg;base64,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");
	}
	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
	}
	
	public Pair<List<Account>, Integer> loadTable(TableParams params) {
		return dao.findTable(params);
	}
	
	public Account loadByCode(String code) {
		return dao.findByCode(code);
	}
	
	public Account getSummaryAccount() {
		return summaryAccount;
	}
	
	public List<Account> loadAll(boolean active) {
		return dao.findAll(active);
	}
	
	public Account create(AccountDto accountDto) {
		Account newAccount = mapper.create(accountDto);
		
		Errors errors = new BeanPropertyBindingResult(newAccount, "Konto");
		validator.validate(newAccount, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(newAccount);
	}
	
	public Account update(int id, AccountDto accountDto) {
		Account account = loadById(id);			
		mapper.update(account, accountDto);
		
		Errors errors = new BeanPropertyBindingResult(account, "Konto");
		validator.validate(account, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(account);
	}
	
	public List<Pair<Account, Entry>> getAccountSummaries(String accountCode) {	
		List<String> accountCodes = accountCode != null
				? List.of(accountCode)
				: dao.findAll(true).stream().map(Account::getCode).collect(Collectors.toList());
		
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

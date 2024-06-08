package pl.telech.tmoney.bank.logic;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.config.SummaryAccount;
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
	
	final AccountDAO dao;
	final EntryDAO entryDao; //TODO wywalić
	final AccountMapper mapper;
	final AccountValidator validator;
	final SummaryAccount summaryAccount;

	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
	}
	
	public Pair<List<Account>, Integer> loadTable(TableParams params) {
		return dao.findTable(params);
	}
	
	public Account loadByCode(String code) {
		if (code.equals(summaryAccount.getCode())) {
			return getSummaryAccount();
		}
		else {
			return dao.findByCode(code);
		}
	}
	
	public Account getSummaryAccount() {
		var result = new Account();
		result.setCode(summaryAccount.getCode());
		result.setName(summaryAccount.getName());
		result.setColor(summaryAccount.getColor());
		result.setIcon(summaryAccount.getIcon());
		result.setLogo(summaryAccount.getLogo());
		return result;
	}
	
	public List<Account> loadAll(boolean active) {
		return dao.findAll(active);
	}
	
	public List<Account> loadAllActiveWithSummary() {
		List<Account> result = dao.findAll(true);
		result.add(getSummaryAccount());
		return result;
	}
	
	public List<Account> loadWithEntries(LocalDate dateFrom, LocalDate dateTo) {
		return dao.findWithEntries(dateFrom, dateTo);
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
		List<Pair<Account, Entry>> result = accountCodes.stream()
				.map(code -> {
					var account = dao.findByCode(code);
					Optional<Entry> entry = entryDao.findLastByAccountBeforeDate(account.getId(), LocalDate.now().plusDays(1));
					return Pair.of(account, entry.orElse(null));
				})
				.collect(Collectors.toList());
		
		if (accountCodes.size() > 1) {
			Entry lastEntry = result.stream()
				        .filter(as -> as.getKey().isIncludeInSummary() && as.getValue() != null)
				        .map(as -> as.getValue())
				        .max(Comparator.comparing(entry -> entry.getDate() + ":" + entry.getId()))
				        .get();
			 
			result.add(Pair.of(getSummaryAccount(), lastEntry));
		}
		
		return result;
	}
	
}

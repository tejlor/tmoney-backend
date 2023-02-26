package pl.telech.tmoney.bank.logic.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.AccountDAO;
import pl.telech.tmoney.bank.model.entity.Account;

@Component
@RequiredArgsConstructor
public class ValidationDataProvider {

	public static final String OTHER_ACCOUNTS = "ValidationDataProvider.otherAccounts";
	
	final AccountDAO accountDAO;
	
	
	@Cacheable(OTHER_ACCOUNTS)
	public List<Account> getOtherAccounts(Account account) {
		return accountDAO.findAll().stream()
				.filter(acc -> !acc.equals(account))
				.collect(Collectors.toList());
	}
}

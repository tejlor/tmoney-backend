package pl.telech.tmoney.bank.logic.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.AccountDAO;
import pl.telech.tmoney.bank.dao.CategoryDAO;
import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.dao.TransferDefinitionDAO;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;

@Component
@RequiredArgsConstructor
public class ValidationDataProvider {

	public static final String OTHER_ACCOUNTS = "ValidationDataProvider.otherAccounts";
	public static final String OTHER_CATEGORIES = "ValidationDataProvider.otherCategories";
	public static final String OTHER_ACCOUNT_ENTRIES = "ValidationDataProvider.otherAccountEntries";
	public static final String OTHER_TRANSFER_DEFINITIONS = "ValidationDataProvider.otherTransferDefinitions";
	
	final AccountDAO accountDAO;
	final CategoryDAO categoryDAO;
	final EntryDAO entryDAO;
	final TransferDefinitionDAO transferDefinitionDAO;
	
	
	@Cacheable(OTHER_ACCOUNTS)
	public List<Account> getOtherAccounts(Account account) {
		return accountDAO.findAll().stream()
				.filter(acc -> !acc.equals(account))
				.collect(Collectors.toList());
	}
	
	@Cacheable(OTHER_CATEGORIES)
	public List<Category> getOtherCategories(Category category) {
		return categoryDAO.findAll().stream()
				.filter(cat -> !cat.equals(category))
				.collect(Collectors.toList());
	}
	
	@Cacheable(OTHER_ACCOUNT_ENTRIES)
	public List<Entry> getOtherAccountEntries(Entry entry) {
		return entryDAO.findByAccountIdAndDate(entry.getAccount().getId(), entry.getDate()).stream()
				.filter(ent -> !ent.equals(entry))
				.collect(Collectors.toList());
				
	}
	
	@Cacheable(OTHER_TRANSFER_DEFINITIONS)
	public List<TransferDefinition> getOtherTransferDefinitions(TransferDefinition definition) {
		return transferDefinitionDAO.findAll().stream()
				.filter(def -> !def.equals(definition))
				.collect(Collectors.toList());
	}
}

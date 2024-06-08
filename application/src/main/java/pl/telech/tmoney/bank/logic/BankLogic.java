package pl.telech.tmoney.bank.logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.bank.logic.transaction.CategoryResolver;
import pl.telech.tmoney.bank.logic.transaction.TransactionsFileParser;
import pl.telech.tmoney.bank.model.data.BalanceRequest;
import pl.telech.tmoney.bank.model.data.TransferRequest;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.utils.TStreamUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankLogic {

	final AccountLogic accountLogic;
	final CategoryLogic categoryLogic;
	final EntryLogic entryLogic;
	final TransferDefinitionLogic transferDefinitionLogic;
	final TransactionsFileParser transactionsFileParser;
	
	
	public Entry balanceAccount(BalanceRequest request) {
		Account account = accountLogic.loadById(request.getAccountId());
		Category category = account.getBalancingCategory();	
		if (category == null) {
			throw new TMoneyException("Balancing category is not defined");
		}
		
		Entry lastEntry = entryLogic.loadLastByAccount(account.getId()).orElseThrow();
		
		var entry = new Entry();
		entry.setAccount(account);
		entry.setCategory(category);
		entry.setName(category.getDefaultName());
		entry.setDate(request.getDate());
		entry.setAmount(request.getBalance().subtract(lastEntry.getBalance()));
		entry.setDescription(category.getDefaultDescription());	
		return entryLogic.saveAndRecalculate(entry);	
	}
	
	public void createTransfer(TransferRequest request) {
		TransferDefinition definition = transferDefinitionLogic.loadById(request.getTransferDefinitionId());
		
		var sourceEntry = new Entry();
		sourceEntry.setAccount(definition.getSourceAccount());
		sourceEntry.setCategory(definition.getCategory());
		sourceEntry.setDate(request.getDate());
		sourceEntry.setAmount(request.getAmount().negate());
		sourceEntry.setName(request.getName());
		sourceEntry.setDescription(request.getDescription());
		entryLogic.saveAndRecalculate(sourceEntry);
		
		var destEntry = new Entry();
		destEntry.setAccount(definition.getDestinationAccount());
		destEntry.setCategory(definition.getCategory());
		destEntry.setDate(request.getDate());
		destEntry.setAmount(request.getAmount());
		destEntry.setName(request.getName());
		destEntry.setDescription(request.getDescription());
		entryLogic.saveAndRecalculate(destEntry);		
	}
	
	public List<Entry> parseTransactionsFile(MultipartFile file) {
		if (!file.getContentType().equals("application/vnd.ms-excel")) {
			throw new TMoneyException("Niepoprawny plik. Można importowac tylko pliki csv.");
		}
		
		String content;
		try {
			content = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
		} 
		catch (IOException e) {
			throw new TMoneyException("Błąd podczas odczytu zawartości pliku", e);
		}
		
		List<Entry> entriesFromFile = transactionsFileParser.parseTransactionsFile(content);	
		log.info("Loaded {} transactions from file", entriesFromFile.size());
		
		List<Entry> entriesInDb = entryLogic.loadByExternalIds(TStreamUtils.map(entriesFromFile, Entry::getExternalId));
		Set<String> existedExternalIds = TStreamUtils.mapToSet(entriesInDb, Entry::getExternalId);		
		entriesFromFile.removeIf(transaction -> existedExternalIds.contains(transaction.getExternalId()));	
		log.info("Skipped transactions: {}. Remaining: {}", entriesInDb.size(), entriesFromFile.size());
		
		Map<Integer, Category> categories = categoryLogic.loadAll().stream().collect(Collectors.toMap(Category::getId, Function.identity()));
		entriesFromFile.forEach(entry -> resolveCategory(entry, categories));
		
		return entriesFromFile;
	}
	
	private void resolveCategory(Entry entry, Map<Integer, Category> categories) {
		CategoryResolver.resolve(entry).ifPresent(id -> entry.setCategory(categories.get(id)));
	}
}

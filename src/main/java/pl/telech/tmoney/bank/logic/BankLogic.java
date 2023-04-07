package pl.telech.tmoney.bank.logic;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.model.data.BalanceRequest;
import pl.telech.tmoney.bank.model.data.TransferRequest;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.model.exception.TMoneyException;

@Service
@RequiredArgsConstructor
public class BankLogic {

	final AccountLogic accountLogic;
	final EntryLogic entryLogic;
	final TransferDefinitionLogic transferDefinitionLogic;
	
	
	public void balanceAccount(BalanceRequest request) {
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
		entryLogic.saveAndRecalculate(entry);	
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
}

package pl.telech.tmoney.bank.logic;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.model.data.BalanceRequest;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.exception.TMoneyException;

@Service
@RequiredArgsConstructor
public class BankLogic {

	final AccountLogic accountLogic;
	final EntryLogic entryLogic;
	
	
	public void balanceAccount(BalanceRequest request) {
		Account account = accountLogic.loadById(request.getAccountId());
		Category category = account.getBalancingCategory();	
		if (category == null) {
			throw new TMoneyException("Balancing category is not defined");
		}
		
		Entry lastEntry = entryLogic.loadLastByAccount(account.getId());
		
		var entry = new Entry();
		entry.setAccount(account);
		entry.setCategory(category);
		entry.setName(category.getDefaultName());
		entry.setDate(request.getDate());
		entry.setAmount(request.getBalance().subtract(lastEntry.getBalance()));
		entry.setDescription(category.getDefaultDescription());
		
		entryLogic.saveAndRecalculate(entry);	
	}
}

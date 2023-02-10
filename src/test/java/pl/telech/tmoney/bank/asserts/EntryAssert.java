package pl.telech.tmoney.bank.asserts;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.asserts.EntityAssert;


public class EntryAssert extends EntityAssert<Entry, EntryDto> {

	
	private EntryAssert(EntryDto result) {
		super(result);
		
		addCondition("accountId", Pair.of(Entry::getAccountId, dto -> dto.getAccount().getId()));	
		addCondition("account", Pair.of(entity -> entity.getAccount().getId(), dto -> dto.getAccount().getId()));		
		addCondition("date", Pair.of(Entry::getDate, EntryDto::getDate));		
		addCondition("category", Pair.of(entity -> entity.getCategory().getId(), dto -> dto.getCategory().getId()));	
		addCondition("name", Pair.of(Entry::getName, EntryDto::getName));	
		addCondition("amount", Pair.of(Entry::getAmount, EntryDto::getAmount));	
		addCondition("description", Pair.of(Entry::getDescription, EntryDto::getDescription));	
		addCondition("balance", Pair.of(Entry::getBalance, EntryDto::getBalance));
		addCondition("balanceOverall", Pair.of(Entry::getBalanceOverall, EntryDto::getBalanceOverall));
		
		addUpdateSkipFields("accountId", "account", "balance", "balanceOverall");
	}
	
	public static EntryAssert assertThatDto(EntryDto result) {
		return new EntryAssert(result);
	}
		
}

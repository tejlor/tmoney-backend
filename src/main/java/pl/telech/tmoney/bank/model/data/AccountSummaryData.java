package pl.telech.tmoney.bank.model.data;

import lombok.Value;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.commons.model.interfaces.Loggable;

@Value
public class AccountSummaryData implements Loggable {
	
	AccountDto account;
	EntryDto entry;
}

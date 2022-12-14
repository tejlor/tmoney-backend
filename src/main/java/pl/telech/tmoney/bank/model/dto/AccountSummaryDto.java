package pl.telech.tmoney.bank.model.dto;

import lombok.Value;
import pl.telech.tmoney.commons.model.interfaces.Loggable;

@Value
public class AccountSummaryDto implements Loggable {
	
	AccountDto account;
	EntryDto entry;
}

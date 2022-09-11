package pl.telech.tmoney.bank.model.dto;

import static lombok.AccessLevel.PRIVATE;

import lombok.Value;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.interfaces.Loggable;

@Value
@FieldDefaults(level = PRIVATE)
public class AccountSummaryDto implements Loggable {
	
	AccountDto account;
	EntryDto entry;
}

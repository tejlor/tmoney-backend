package pl.telech.tmoney.bank.model.dto;

import static lombok.AccessLevel.PRIVATE;

import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(level = PRIVATE)
public class AccountWithLastEntryDto {
	
	AccountDto account;
	EntryDto lastEntry;
}

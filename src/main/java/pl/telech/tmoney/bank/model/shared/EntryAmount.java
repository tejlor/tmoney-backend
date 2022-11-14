package pl.telech.tmoney.bank.model.shared;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class EntryAmount {

	LocalDate date;
	BigDecimal amount;
}

package pl.telech.tmoney.bank.model.shared;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.entity.Account;

@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class AccountReportData {

	Account account;
	BigDecimal initialBalance;
	BigDecimal finalBalance;
	BigDecimal incomesSum;
	BigDecimal outcomesSum;
		
	public BigDecimal calcBalance() {
		return incomesSum.subtract(outcomesSum);
	}
}

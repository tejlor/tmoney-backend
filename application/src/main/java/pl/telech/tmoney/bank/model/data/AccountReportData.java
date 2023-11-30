package pl.telech.tmoney.bank.model.data;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import pl.telech.tmoney.bank.model.entity.Account;

@Getter
@Builder
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

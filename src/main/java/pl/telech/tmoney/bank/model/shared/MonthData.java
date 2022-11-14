package pl.telech.tmoney.bank.model.shared;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.Month;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class MonthData implements Comparable<MonthData> {
	
	Month month;
	BigDecimal income;
	BigDecimal outcome;

	@Setter
	BigDecimal balance;
	
	public BigDecimal calcProfit() {
		return income.subtract(outcome);
	}

	@Override
	public int compareTo(MonthData other) {
		return month.compareTo(other.month);
	}
	
}

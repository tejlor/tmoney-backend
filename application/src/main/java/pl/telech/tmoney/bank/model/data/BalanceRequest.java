package pl.telech.tmoney.bank.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BalanceRequest {

	int accountId;
	LocalDate date;
	BigDecimal balance;
}

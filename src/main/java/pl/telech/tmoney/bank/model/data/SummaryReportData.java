package pl.telech.tmoney.bank.model.data;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import pl.telech.tmoney.bank.dao.data.CategoryAmount;

@Getter
@Builder
public class SummaryReportData {

	BigDecimal initialBalance;
	BigDecimal finalBalance;
	List<CategoryAmount> incomes;
	List<CategoryAmount> outcomes;
	List<CategoryAmount> profits;
	BigDecimal incomesSum;
	BigDecimal outcomesSum;
	BigDecimal profitsSum;
}

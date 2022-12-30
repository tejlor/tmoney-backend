package pl.telech.tmoney.bank.model.data;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import pl.telech.tmoney.bank.dao.data.CategoryAmount;
import pl.telech.tmoney.commons.utils.TUtils;

@Getter
@Builder
public class SummaryReportData {

	BigDecimal initialBalance;
	BigDecimal finalBalance;
	List<CategoryAmount> incomes;
	List<CategoryAmount> outcomes;
		
	public BigDecimal calcIncomesSum() {
		return TUtils.sum(incomes, CategoryAmount::getAmount);
	}
	
	public BigDecimal calcOutcomesSum() {
		return TUtils.sum(outcomes, CategoryAmount::getAmount);
	}
	
	public BigDecimal calcBalance() {
		return calcIncomesSum().subtract(calcOutcomesSum());
	}
}

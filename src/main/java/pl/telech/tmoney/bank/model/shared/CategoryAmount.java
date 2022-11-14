package pl.telech.tmoney.bank.model.shared;

import java.math.BigDecimal;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class CategoryAmount implements Comparable<CategoryAmount> {

	String categoryName;
	BigDecimal amount;
	
	@Override
	public int compareTo(CategoryAmount other) {
		return other.amount.compareTo(amount);
	}
}

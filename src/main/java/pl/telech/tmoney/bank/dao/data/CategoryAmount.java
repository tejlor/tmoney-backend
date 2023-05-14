package pl.telech.tmoney.bank.dao.data;

import java.math.BigDecimal;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class CategoryAmount implements Comparable<CategoryAmount> {

	public static final String TYPE = "pl.telech.tmoney.bank.dao.data.CategoryAmount";
	
	String categoryName;
	BigDecimal amount;
	
	/*
	 * Order by amount descending.
	 */
	@Override
	public int compareTo(CategoryAmount other) {
		return other.amount.compareTo(amount);
	}
}

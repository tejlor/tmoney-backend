package pl.telech.tmoney.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

	public BigDecimal decimal(String str) {
		return new BigDecimal(str.replace(" ", ""));
	}
	
	public LocalDate date(String str) {
		return LocalDate.parse(str);
	}
}

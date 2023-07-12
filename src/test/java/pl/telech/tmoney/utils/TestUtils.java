package pl.telech.tmoney.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

	public static BigDecimal dec(String str) {
		return new BigDecimal(str.replace(" ", "").replace(",", "."));
	}
	
	public static LocalDate date(String str) {
		return LocalDate.parse(str);
	}
}

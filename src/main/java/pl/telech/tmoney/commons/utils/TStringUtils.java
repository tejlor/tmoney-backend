package pl.telech.tmoney.commons.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TStringUtils {

	public static String toCamelCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}

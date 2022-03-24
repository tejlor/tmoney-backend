package pl.telech.tmoney.adm.logic.interfaces;

import java.math.BigDecimal;

public interface SettingLogic {

	int loadIntValue(String name);
	BigDecimal loadDecimalValue(String name);
	boolean loadBoolValue(String name);
	String loadStringValue(String name);
}

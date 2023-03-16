package pl.telech.tmoney.adm.asserts;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.asserts.EntityAssert;


public class SettingAssert extends EntityAssert<Setting, SettingDto> {

	private SettingAssert(SettingDto result) {
		super(result);
		
		addCondition("firstName", 	Pair.of(Setting::getName, SettingDto::getName));
		addCondition("lastName", 	Pair.of(Setting::getValue, SettingDto::getValue));

	}
	
	public static SettingAssert assertThatDto(SettingDto result) {
		return new SettingAssert(result);
	}
		
}

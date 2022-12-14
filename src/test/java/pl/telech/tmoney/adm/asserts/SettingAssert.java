package pl.telech.tmoney.adm.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;


public class SettingAssert extends AbstractAssert<SettingAssert, SettingDto> {

	private SettingAssert(SettingDto actual) {
		super(actual, SettingAssert.class);
	}
	
	public static SettingAssert assertThat(SettingDto actual) {
		return new SettingAssert(actual);
	}
	
	public SettingAssert isEqualTo(Setting entity) {
		var asserts = new SoftAssertions();
		asserts.assertThat(actual.getId()).isEqualTo(entity.getId());
		asserts.assertThat(actual.getName()).isEqualTo(entity.getName());
		asserts.assertThat(actual.getValue()).isEqualTo(entity.getValue());
		asserts.assertAll();
		return this;
	}
		
}

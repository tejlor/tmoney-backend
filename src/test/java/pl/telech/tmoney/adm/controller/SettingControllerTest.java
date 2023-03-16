package pl.telech.tmoney.adm.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.adm.asserts.SettingAssert;
import pl.telech.tmoney.adm.helper.SettingHelper;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.utils.BaseMvcTest;


class SettingControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/settings";
	
	@Autowired
	SettingHelper settingHelper;
	
	
	@Test
	void getAll() throws Exception {
		// given
		Setting setting0 = settingHelper.save("key1", "val1");
		Setting setting1 = settingHelper.save("key2", "val2");
				
		// when
		List<SettingDto> response = get(baseUrl, new TypeReference<List<SettingDto>>() {});
		
		// then	
		assertThat(response).hasSize(2);
		SettingAssert.assertThatDto(response.get(0)).isMappedFrom(setting0);
		SettingAssert.assertThatDto(response.get(1)).isMappedFrom(setting1);
	}
	
}

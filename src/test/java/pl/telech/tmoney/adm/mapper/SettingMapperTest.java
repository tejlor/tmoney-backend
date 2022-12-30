package pl.telech.tmoney.adm.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import pl.telech.tmoney.adm.asserts.SettingAssert;
import pl.telech.tmoney.adm.builder.SettingBuilder;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;

@RunWith(SpringRunner.class)
@Import({SettingBuilder.class, SettingMapperImpl.class})
public class SettingMapperTest {

	@Autowired
	SettingBuilder builder;

	@Autowired
	SettingMapper mapper;

	@Test
	public void testToDto() {
		// given
		Setting setting = builder.build();

		// when
		SettingDto result = mapper.toDto(setting);

		// then
		assertThat(result).isNotNull();
		SettingAssert.assertThat(result).isEqualTo(setting);
	}
}

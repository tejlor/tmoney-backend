package pl.telech.tmoney.adm.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.adm.asserts.SettingAssert;
import pl.telech.tmoney.adm.builder.SettingBuilder;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;

@ExtendWith(SpringExtension.class)
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

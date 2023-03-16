package pl.telech.tmoney.adm.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.adm.asserts.SettingAssert;
import pl.telech.tmoney.adm.builder.SettingBuilder;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperTest;

@ExtendWith(SpringExtension.class)
@Import({SettingBuilder.class, SettingMapperImpl.class})
public class SettingMapperTest extends EntityMapperTest<Setting, SettingDto> {

	@Autowired
	SettingBuilder builder;

	@Autowired
	SettingMapper mapper;
	
	@Override
	protected AbstractBuilder<Setting> getBuilder() {
		return builder;
	}

	@Override
	protected EntityMapper<Setting, SettingDto> getMapper() {
		return mapper;
	}

	@Override
	protected Class<SettingAssert> getAssertionsClass() {
		return SettingAssert.class;
	}
}

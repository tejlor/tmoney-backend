package pl.telech.tmoney.adm.mapper;

import org.mapstruct.Mapper;

import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface SettingMapper {
	
	SettingDto toDto(Setting entity);
}

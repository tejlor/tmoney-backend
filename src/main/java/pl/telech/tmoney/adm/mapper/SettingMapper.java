package pl.telech.tmoney.adm.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface SettingMapper extends EntityMapper<Setting, SettingDto> {
	
	SettingDto toDto(Setting entity);
	
	List<SettingDto> toDtoList(Collection<Setting> entities);
	
	Setting toEntity(SettingDto dto);
	
	@Named("create")
	@InheritConfiguration(name = "create")
	Setting create(SettingDto dto);
	
	@InheritConfiguration(name = "update")
	Setting update(@MappingTarget Setting entity, SettingDto dto);
}

package pl.telech.tmoney.commons.mapper;

import org.mapstruct.*;

import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@MapperConfig
public interface EntityMapperConfig {
		
	@Mapping(target = "id", ignore = true)
	AbstractEntity toNewEntity(AbstractDto dto);
	
	@InheritConfiguration(name = "toNewEntity")
	AbstractEntity update(AbstractDto dto, @MappingTarget AbstractEntity entity);
}

package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.*;

import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class, uses = {AccountMapper.class, CategoryMapper.class})
public interface TransferDefinitionMapper extends EntityMapper<TransferDefinition, TransferDefinitionDto> {
	
	TransferDefinitionDto toDto(TransferDefinition entity);
	
	List<TransferDefinitionDto> toDtoList(Collection<TransferDefinition> entities);
	
	@Mapping(target = "sourceAccount", qualifiedByName = "toEntity")
	@Mapping(target = "destinationAccount", qualifiedByName = "toEntity")
	@Mapping(target = "category", qualifiedByName = "toEntity")
	TransferDefinition toEntity(TransferDefinitionDto dto);
	
	@Named("create")
	@Mapping(target = "sourceAccount", qualifiedByName = "toEntity")
	@Mapping(target = "destinationAccount", qualifiedByName = "toEntity")
	@Mapping(target = "category", qualifiedByName = "toEntity")
	@InheritConfiguration(name = "create")
	TransferDefinition create(TransferDefinitionDto dto);
	
	@InheritConfiguration(name = "update")
	@Mapping(target = "sourceAccount", qualifiedByName = "toEntity")
	@Mapping(target = "destinationAccount", qualifiedByName = "toEntity")
	@Mapping(target = "category", qualifiedByName = "toEntity")
	TransferDefinition update(@MappingTarget TransferDefinition entity, TransferDefinitionDto dto);
}

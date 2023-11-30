package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.*;

import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class, uses = {AccountMapper.class, CategoryMapper.class})
public interface EntryMapper extends EntityMapper<Entry, EntryDto> {
	
	EntryDto toDto(Entry entity);
	
	List<EntryDto> toDtoList(Collection<Entry> entities);
	
	@Mapping(target = "account", qualifiedByName = "toEntity")
	@Mapping(target = "category", qualifiedByName = "toEntity")
	Entry toEntity(EntryDto dto);
	
	@Named("create")
	@Mapping(target = "account", qualifiedByName = "toEntity")
	@Mapping(target = "category", qualifiedByName = "toEntity")
	@InheritConfiguration(name = "create")
	Entry create(EntryDto dto);
	
	@InheritConfiguration(name = "update")
	@Mapping(target = "account", ignore = true)
	@Mapping(target = "category", qualifiedByName = "toEntity")
	@Mapping(target = "balance", ignore = true)
	@Mapping(target = "balanceOverall", ignore = true)
	Entry update(@MappingTarget Entry entity, EntryDto dto);
}

package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class, uses = {AccountMapper.class, CategoryMapper.class})
public interface EntryMapper {
	
	EntryDto toDto(Entry entity);
	
	List<EntryDto> toDtoList(Collection<Entry> entity);
	
	@InheritConfiguration(name = "toNewEntity")
	@Mapping(target = "account", qualifiedByName = "toEntity")
	@Mapping(target = "category", qualifiedByName = "toEntity")
	Entry toNewEntity(EntryDto dto);
	
	@InheritConfiguration(name = "update")
	@Mapping(target = "accountId", ignore = true)
	@Mapping(target = "account", ignore = true)
	@Mapping(target = "categoryId", ignore = true)
	@Mapping(target = "category", qualifiedByName = "toEntity")
	@Mapping(target = "balance", ignore = true)
	@Mapping(target = "balanceOverall", ignore = true)
	Entry update(EntryDto dto, @MappingTarget Entry entity);
}

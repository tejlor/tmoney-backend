package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.*;

import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class, uses = CategoryMapper.class)
public interface AccountMapper extends EntityMapper<Account, AccountDto> {
	
	AccountDto toDto(Account entity);
	
	List<AccountDto> toDtoList(Collection<Account> entities);
	
	@Named("toEntity")
	Account toEntity(AccountDto dto);

	@Named("create")
	@InheritConfiguration(name = "create")
	@Mapping(target = "balancingCategory", qualifiedByName = "toEntity")
	Account create(AccountDto dto);
	
	@InheritConfiguration(name = "update")
	@Mapping(target = "code", ignore = true)
	@Mapping(target = "balancingCategory", qualifiedByName = "toEntity")
	Account update(@MappingTarget Account entity, AccountDto dto);
}

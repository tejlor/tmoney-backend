package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.*;

import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface AccountMapper {
	
	AccountDto toDto(Account entity);
	
	List<AccountDto> toDtoList(Collection<Account> entity);
	
	@Named("toEntity")
	Account toEntity(AccountDto dto);
	
	@Named("toNewEntity")
	@InheritConfiguration(name = "toNewEntity")
	Account toNewEntity(AccountDto dto);
	
	@Mapping(target = "code", ignore = true)
	@InheritConfiguration(name = "update")
	Account update(AccountDto dto, @MappingTarget Account entity);
}

package pl.telech.tmoney.adm.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface UserMapper extends EntityMapper<User, UserDto> {
	
AccountDto toDto(Account entity);
	
	List<UserDto> toDtoList(Collection<User> entities);
	
	@Named("toEntity")
	User toEntity(UserDto dto);

	@Named("create")
	@InheritConfiguration(name = "create")
	User create(UserDto dto);
	
	@InheritConfiguration(name = "update")
	User update(@MappingTarget User entity, UserDto dto);
}

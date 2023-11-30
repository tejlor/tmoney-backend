package pl.telech.tmoney.adm.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.*;

import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;
import org.mapstruct.Mapping;

@Mapper(config = EntityMapperConfig.class)
public interface UserMapper extends EntityMapper<User, UserDto> {
	
	UserDto toDto(User entity);
	
	List<UserDto> toDtoList(Collection<User> entities);
	
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	@Named("toEntity")
	User toEntity(UserDto dto);

	@Named("create")
	@InheritConfiguration(name = "create")
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	User create(UserDto dto);
	
	@InheritConfiguration(name = "update")
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	User update(@MappingTarget User entity, UserDto dto);
}

package pl.telech.tmoney.adm.mapper;

import org.mapstruct.Mapper;

import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface UserMapper {
	
	UserDto toDto(User entity);
}

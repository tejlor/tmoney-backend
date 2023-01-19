package pl.telech.tmoney.adm.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.adm.asserts.UserAssert;
import pl.telech.tmoney.adm.builder.UserBuilder;
import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperTest;

@ExtendWith(SpringExtension.class)
@Import({UserBuilder.class, UserMapperImpl.class})
public class UserMapperTest extends EntityMapperTest<User, UserDto> {

	@Autowired
	UserBuilder builder;

	@Autowired
	UserMapper mapper;
	
	@Override
	protected AbstractBuilder<User> getBuilder() {
		return builder;
	}

	@Override
	protected EntityMapper<User, UserDto> getMapper() {
		return mapper;
	}

	@Override
	protected Class<UserAssert> getAssertionsClass() {
		return UserAssert.class;
	}
}
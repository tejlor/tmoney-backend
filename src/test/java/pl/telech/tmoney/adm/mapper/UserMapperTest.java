package pl.telech.tmoney.adm.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.adm.asserts.UserAssert;
import pl.telech.tmoney.adm.builder.UserBuilder;
import pl.telech.tmoney.adm.model.dto.UserDto;
import pl.telech.tmoney.adm.model.entity.User;

@ExtendWith(SpringExtension.class)
@Import({UserBuilder.class, UserMapperImpl.class})
public class UserMapperTest {

	@Autowired
	UserBuilder builder;

	@Autowired
	UserMapper mapper;

	@Test
	public void testToDto() {
		// given
		User user = builder.build();

		// when
		UserDto result = mapper.toDto(user);

		// then
		assertThat(result).isNotNull();
		UserAssert.assertThat(result).isEqualTo(user);
	}
}

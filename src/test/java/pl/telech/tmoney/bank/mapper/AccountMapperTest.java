package pl.telech.tmoney.bank.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.asserts.AccountAssert;
import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;

@ExtendWith(SpringExtension.class)
@Import({AccountBuilder.class, AccountMapperImpl.class})
public class AccountMapperTest {

	@Autowired
	AccountBuilder builder;

	@Autowired
	AccountMapper mapper;

	@Test
	public void testToDto() {
		// given
		Account entity = builder.id(1).build();

		// when
		AccountDto result = mapper.toDto(entity);

		// then
		assertThat(result).isNotNull();
		AccountAssert.assertThatDto(result).isMappedFrom(entity);
	}
	
	@Test
	public void testToDtoList() {
		// given
		List<Account> list = List.of(builder.id(1).build());

		// when
		List<AccountDto> result = mapper.toDtoList(list);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSameSizeAs(list);
		AccountAssert.assertThatDto(result.get(0)).isMappedFrom(list.get(0));
	}
	
	@Test
	public void testToEntity() {
		// given
		AccountDto dto = mapper.toDto(builder.id(1).build());

		// when
		Account result = mapper.toEntity(dto);

		// then
		assertThat(result).isNotNull();
		AccountAssert.assertThatDto(dto).isMappedFrom(result);
	}
	
	@Test
	public void testToNewEntity() {
		// given
		AccountDto dto = mapper.toDto(builder.id(1).build());

		// when
		Account result = mapper.toNewEntity(dto);

		// then
		assertThat(result).isNotNull();
		AccountAssert.assertThatDto(dto).creates(result);
	}

	@Test
	public void testUpdate() {
		// given
		AccountDto dto = mapper.toDto(builder.id(1).build());

		// when
		Account updatedEntity = new Account();
		Account result = mapper.update(dto, updatedEntity);

		// then
		assertThat(result).isSameAs(updatedEntity);
		AccountAssert.assertThatDto(dto).updates(result);
	}
}

package pl.telech.tmoney.bank.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.asserts.AccountAssert;
import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperTest;

@ExtendWith(SpringExtension.class)
@Import({AccountBuilder.class, AccountMapperImpl.class})
public class AccountMapperTest extends EntityMapperTest<Account, AccountDto> {

	@Autowired
	AccountBuilder builder;

	@Autowired
	AccountMapper mapper;
	
	@Override
	protected AbstractBuilder<Account> getBuilder() {
		return builder;
	}

	@Override
	protected EntityMapper<Account, AccountDto> getMapper() {
		return mapper;
	}

	@Override
	protected Class<AccountAssert> getAssertionsClass() {
		return AccountAssert.class;
	}

}

package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class AccountControllerTest extends BaseTest {

	@Autowired
	AccountController controller;
	
	@Test
	@Transactional
	public void getAll() {	
		// given
		Account account0 = setupAccount("Konto bankowe");
		Account account1 = setupAccount("Dom");
		Account account2 = setupAccount("Konto maklerskie");
		flush();
		
		// when
		List<AccountDto> result = controller.getAll();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertAccount(result.get(0), account0, true);
		assertAccount(result.get(1), account1, true);
		assertAccount(result.get(2), account2, true);
	}
	
	@Test
	@Transactional
	public void getById() {	
		// given
		Account account = setupAccount("Konto bankowe");
		flush();
		
		// when
		AccountDto result = controller.getById(account.getId());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertAccount(result, account, true);
	}
	
	@Test
	@Transactional
	public void create() {	
		// given
		flush();
		
		// when
		Account account = buildAccount("Konto bankowe");
		AccountDto result = controller.create(new AccountDto(account));	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertAccount(result, account, false);
	}
	
	@Test
	@Transactional
	public void update() {	
		// given
		Account account = setupAccount("Konto bankowe");
		flush();
		
		// when
		AccountDto dto = new AccountDto(account);
		dto.setActive(false);
		AccountDto result = controller.update(account.getId(), dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		Account updatedAccount = load(Account.class, account.getId());
		assertAccount(result, updatedAccount, true);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertAccount(AccountDto dto, Account model, boolean withId) {
		if(withId) {
			assertThat(dto.getId()).isEqualTo(model.getId());
		}
		assertThat(dto.getCode()).isEqualTo(model.getCode());
		assertThat(dto.getName()).isEqualTo(model.getName());
		assertThat(dto.getActive()).isEqualTo(model.getActive());
		assertThat(dto.getColor()).isEqualTo(model.getColor());
		assertThat(dto.getOrderNo()).isEqualTo(model.getOrderNo());
	}
	
	private Account setupAccount(String name) {
		return new AccountBuilder()
			.name(name)
			.save(entityManager);
	}
	
	private Account buildAccount(String name) {
		return new AccountBuilder()
			.name(name)
			.build();
	}
	
}

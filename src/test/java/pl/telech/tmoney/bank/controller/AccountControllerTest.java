package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.dto.AccountWithEntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.enums.Mode;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class AccountControllerTest extends BaseTest {

	@Autowired
	AccountController controller;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	EntryHelper eentryHelper;
	
	@Test
	@Transactional
	public void getAll() {	
		// given
		Account account0 = accountHelper.save("Konto bankowe");
		Account account1 = accountHelper.save("Dom");
		Account account2 = accountHelper.save("Konto maklerskie");
		flush();
		
		// when
		List<AccountDto> result = controller.getAll();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		accountHelper.assertEqual(result.get(0), account0, Mode.GET);
		accountHelper.assertEqual(result.get(1), account1, Mode.GET);
		accountHelper.assertEqual(result.get(2), account2, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getSummary() {
		// given
		Account account0 = accountHelper.save("Konto bankowe");
		Account account1 = accountHelper.save("Dom");		
		eentryHelper.save("Zakupy", LocalDate.of(2020, 5, 12), account0);
		eentryHelper.save("Fryzjer", LocalDate.of(2020, 7, 12), account1);
		Entry entry0 = eentryHelper.save("ZUS", LocalDate.of(2020, 5, 13),account0);
		Entry entry1 = eentryHelper.save("Zakupy", LocalDate.of(2020, 7, 13),account1);
		flush();
		
		// when
		List<AccountWithEntryDto> result = controller.getSummary();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		accountHelper.assertEqual(result.get(0).getAccount(), account0, Mode.GET);
		accountHelper.assertEqual(result.get(1).getAccount(), account1, Mode.GET);
		eentryHelper.assertEqual(result.get(0).getEntry(), entry0, Mode.GET);
		eentryHelper.assertEqual(result.get(1).getEntry(), entry1, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getById() {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		flush();
		
		// when
		AccountDto result = controller.getById(account.getId());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		accountHelper.assertEqual(result, account, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getByCode() {	
		// given
		Account account = accountHelper.save("Konto bankowe", "BANK");
		flush();
		
		// when
		AccountDto result = controller.getByCode(account.getCode());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		accountHelper.assertEqual(result, account, Mode.GET);
	}
	
	@Test
	@Transactional
	public void create() {	
		// given
		flush();
		
		// when
		Account account = accountHelper.build("Konto bankowe");
		AccountDto result = controller.create(new AccountDto(account));	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		accountHelper.assertEqual(result, account, Mode.CREATE);
	}
	
	@Test
	@Transactional
	public void update() {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		flush();
		
		// when
		AccountDto dto = new AccountDto(account);
		dto.setActive(false);
		AccountDto result = controller.update(account.getId(), dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		Account updatedAccount = load(Account.class, account.getId());
		accountHelper.assertEqual(result, updatedAccount, Mode.GET);
	}
		
}

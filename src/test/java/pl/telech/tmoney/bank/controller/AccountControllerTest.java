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
import pl.telech.tmoney.bank.model.dto.AccountSummaryDto;
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
	EntryHelper entryHelper;
	
	@Test
	@Transactional
	public void getActive() {	
		// given
		Account account0 = accountHelper.save("Konto bankowe");
		Account account1 = accountHelper.save("Dom");
		Account account2 = accountHelper.save("Konto maklerskie");
		Account account3 = accountHelper.save("Konto w innym banku", false);
		flush();
		
		// when
		List<AccountDto> result = controller.getActive();	
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
		Account account0 = accountHelper.save("Konto bankowe", "BANK");
		Account account1 = accountHelper.save("Dom", "HOME");		
		entryHelper.save("Zakupy", LocalDate.of(2020, 5, 12), account0);
		entryHelper.save("Fryzjer", LocalDate.of(2020, 7, 12), account1);
		Entry entry0 = entryHelper.save("ZUS", LocalDate.of(2020, 5, 13), account0);
		Entry entry1 = entryHelper.save("Zakupy", LocalDate.of(2020, 7, 13), account1);
		flush();
		
		// when
		List<AccountSummaryDto> result = controller.getSummary("HOME");	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		accountHelper.assertEqual(result.get(0).getAccount(), account1, Mode.GET);
		entryHelper.assertEqual(result.get(0).getEntry(), entry1, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getSummary_all() {
		// given
		Account account0 = accountHelper.save("Konto bankowe", "BANK");
		Account account1 = accountHelper.save("Dom", "HOME");		
		entryHelper.save("Zakupy", LocalDate.of(2020, 5, 12), account0);
		entryHelper.save("Fryzjer", LocalDate.of(2020, 7, 12), account1);
		Entry entry0 = entryHelper.save("ZUS", LocalDate.of(2020, 5, 13), account0);
		Entry entry1 = entryHelper.save("Zakupy", LocalDate.of(2020, 7, 13), account1);
		flush();
		
		// when
		List<AccountSummaryDto> result = controller.getSummary(null);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		accountHelper.assertEqual(result.get(0).getAccount(), account0, Mode.GET);
		accountHelper.assertEqual(result.get(1).getAccount(), account1, Mode.GET);
		entryHelper.assertEqual(result.get(0).getEntry(), entry0, Mode.GET);
		entryHelper.assertEqual(result.get(1).getEntry(), entry1, Mode.GET);
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

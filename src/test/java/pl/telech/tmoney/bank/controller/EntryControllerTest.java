package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.builder.CategoryBuilder;
import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class EntryControllerTest extends BaseTest {

	@Autowired
	EntryController controller;
	
	@Test
	@Transactional
	public void getAll() {	
		// given
		Account account = saveAccount("Bank");
		Entry entry0 = saveEntry("Zakupy", account);
		Entry entry1 = saveEntry("VAT-7", account);
		Entry entry2 = saveEntry("Fryzjer", account);
		flush();
		
		// when
		List<EntryDto> result = controller.getAll();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertEntry(result.get(0), entry0, Mode.GET);
		assertEntry(result.get(1), entry1, Mode.GET);
		assertEntry(result.get(2), entry2, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getById() {	
		// given
		Account account = saveAccount("Bank");
		Entry entry = saveEntry("Zakupy", account);
		flush();
		
		// when
		EntryDto result = controller.getById(entry.getId());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertEntry(result, entry, Mode.GET);
	}
	
	@Test
	@Transactional
	public void create() {	
		// given
		Account bankAccount = saveAccount("Bank");
		Account homeAccount = saveAccount("Dom");
		Category category = saveCategory("Zakupy");
		saveEntry("Entry B1", bankAccount, "20.00", "20.00", "20.00");
		saveEntry("Entry H1", homeAccount, "40.00", "40.00", "60.00");
		flush();
		
		// when
		Entry entry = buildEntry("Entry B2", bankAccount, category, "30.00");
		EntryDto result = controller.create(new EntryDto(entry));	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertEntry(result, entry, Mode.CREATE);
		assertThat(result.getBalance()).isEqualTo(new BigDecimal("50.00"));
		assertThat(result.getBalanceOverall()).isEqualTo(new BigDecimal("90.00"));
	}
	
	@Test
	@Transactional
	public void update() {	
		// given
		Account bankAccount = saveAccount("Bank");
		Account homeAccount = saveAccount("Dom");
		saveEntry("Entry B1", bankAccount, "20.00",  "20.00",  "20.00"); 
		saveEntry("Entry H1", homeAccount, "40.00",  "40.00",  "60.00"); 
		saveEntry("Entry B2", bankAccount, "30.00",  "50.00",  "90.00"); // edited
		saveEntry("Entry H2", homeAccount, "10.00",  "50.00", "100.00"); 
		saveEntry("Entry B3", bankAccount, "50.00", "100.00", "150.00");
		saveEntry("Entry H3", homeAccount, "60.00", "110.00", "210.00");
		flush();
		
		// when
		EntryDto dto = new EntryDto(load(Entry.class, 3));
		dto.setAmount(new BigDecimal("35.00"));
		dto.setDescription("Nowy opis");
		EntryDto result = controller.update(3, dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		Entry entryB2 = load(Entry.class, 3);
		assertThat(entryB2.getDescription()).isEqualTo(dto.getDescription());
		assertThat(entryB2.getAmount()).isEqualTo(dto.getAmount());
		
		// TODO
	}
	
	@Test
	@Transactional
	public void delete() {	
		// given
		Account account = saveAccount("Bank");
		Entry entry = saveEntry("Zakupy", account);
		flush();
		
		// when
		controller.delete(entry.getId());	
		flushAndClear();
		
		// then
		Entry deletedEntry = load(Entry.class, entry.getId());
		assertThat(deletedEntry).isNull();
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertEntry(EntryDto dto, Entry model, Mode mode) {
		if(mode != Mode.CREATE) {
			assertThat(dto.getId()).isEqualTo(model.getId());
		}
		assertThat(dto.getAccount().getId()).isEqualTo(model.getAccount().getId());
		assertThat(dto.getDate()).isEqualTo(model.getDate());
		assertThat(dto.getCategory().getId()).isEqualTo(model.getCategory().getId());
		assertThat(dto.getName()).isEqualTo(model.getName());
		assertThat(dto.getAmount()).isEqualTo(model.getAmount());
		assertThat(dto.getDescription()).isEqualTo(model.getDescription());
		if(mode == Mode.GET) {
			assertThat(dto.getBalance()).isEqualTo(model.getBalance());
			assertThat(dto.getBalanceOverall()).isEqualTo(model.getBalanceOverall());
		}
	}
	
	private Entry saveEntry(String name, Account account) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.save(entityManager);
	}
	
	private Entry saveEntry(String name, Account account, String amount, String balance, String balanceOverall) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.amount(new BigDecimal(amount))
			.balance(new BigDecimal(balance))
			.balanceOverall(new BigDecimal(balanceOverall))
			.save(entityManager);
	}
	
	private Account saveAccount(String name) {
		return new AccountBuilder()
			.name(name)
			.save(entityManager);
	}
	
	private Category saveCategory(String name) {
		return new CategoryBuilder()
			.name(name)
			.save(entityManager);
	}
	
	private Entry buildEntry(String name, Account account, Category category, String amount) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.category(category)
			.amount(new BigDecimal(amount))
			.build();
	}
}

enum Mode {
	GET, CREATE, UPDATE
}

package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.enums.Mode;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class EntryControllerTest extends BaseTest {

	@Autowired
	EntryController controller;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	EntryHelper entryHelper;
	
	@Test
	@Transactional
	public void getAll() {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		Entry entry0 = entryHelper.save("Zakupy", account);
		Entry entry1 = entryHelper.save("ZUS", account);
		Entry entry2 = entryHelper.save("Fryzjer", account);
		flush();
		
//		// when
//		List<EntryDto> result = controller.getAll();	
//		flushAndClear();
//		
//		// then
//		assertThat(result).isNotNull();
//		assertThat(result).hasSize(3);
//		assertEntry(result.get(0), entry0, Mode.GET);
//		assertEntry(result.get(1), entry1, Mode.GET);
//		assertEntry(result.get(2), entry2, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getById() {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		Entry entry = entryHelper.save("Zakupy", account);
		flush();
		
		// when
		EntryDto result = controller.getById(entry.getId());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		entryHelper.assertEqual(result, entry, Mode.GET);
	}
	
	@Test
	@Transactional
	public void create() {	
		// given
		Account bankAccount = accountHelper.save("Konto bankowe");
		Account homeAccount = accountHelper.save("Dom");
		Category category = categoryHelper.save("Zakupy");
		entryHelper.save("Entry B1", bankAccount, "20.00", "20.00", "20.00");
		entryHelper.save("Entry H1", homeAccount, "40.00", "40.00", "60.00");
		flush();
		
		// when
		Entry entry = entryHelper.build("Entry B2", bankAccount, category, "30.00");
		EntryDto result = controller.create(new EntryDto(entry));	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		entryHelper.assertEqual(result, entry, Mode.CREATE);
		assertThat(result.getBalance()).isEqualTo(new BigDecimal("50.00"));
		assertThat(result.getBalanceOverall()).isEqualTo(new BigDecimal("90.00"));
	}
	
	@Test
	@Transactional
	public void update() {	
		// given
		Account bankAccount = accountHelper.save("Konto bankowe");
		Account homeAccount = accountHelper.save("Dom");
		entryHelper.save("Entry B1", bankAccount, "20.00",  "20.00",  "20.00"); 
		entryHelper.save("Entry H1", homeAccount, "40.00",  "40.00",  "60.00"); 
		Entry entry = entryHelper.save("Entry B2", bankAccount, "30.00",  "50.00",  "90.00");
		entryHelper.save("Entry H2", homeAccount, "10.00",  "50.00", "100.00"); 
		entryHelper.save("Entry B3", bankAccount, "50.00", "100.00", "150.00");
		entryHelper.save("Entry H3", homeAccount, "60.00", "110.00", "210.00");
		flush();
		
		// when
		EntryDto dto = new EntryDto(load(Entry.class, entry.getId()));
		dto.setAmount(new BigDecimal("35.00"));
		dto.setDescription("Nowy opis");
		EntryDto result = controller.update(entry.getId(), dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		Entry entryB2 = load(Entry.class, entry.getId());
		assertThat(entryB2.getDescription()).isEqualTo(dto.getDescription());
		assertThat(entryB2.getAmount()).isEqualTo(dto.getAmount());
	}
	
	@Test
	@Transactional
	public void delete() {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		Entry entry = entryHelper.save("Zakupy", account);
		flush();
		
		// when
		controller.delete(entry.getId());	
		flushAndClear();
		
		// then
		Entry deletedEntry = load(Entry.class, entry.getId());
		assertThat(deletedEntry).isNull();
	}
	
}

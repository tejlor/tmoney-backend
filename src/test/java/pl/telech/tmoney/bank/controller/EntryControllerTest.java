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
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.enums.Mode;
import pl.telech.tmoney.commons.model.dto.TableDataDto;
import pl.telech.tmoney.commons.model.dto.TableDataDto.TableInfoDto;
import pl.telech.tmoney.commons.model.shared.TableParams;
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
		Account account = accountHelper.save("Konto bankowe", "BANK");
		entryHelper.save("Zakupy", account);
		entryHelper.save("Zus", account);
		entryHelper.save("VAT-7", account);
		entryHelper.save("Leasing", account);
		entryHelper.save("OneDrive", account);
		entryHelper.save("Benzyna", account);
		entryHelper.save("Autostrada", account);
		entryHelper.save("Czapka N", account);
		flush();
		
		// when
		TableDataDto<EntryDto> result = controller.getAll("BANK", 1, 2, "z", "name ASC");	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();

		TableParams tableParams = result.getTableParams();
		assertThat(tableParams.getPageNo()).isEqualTo(1);
		assertThat(tableParams.getPageSize()).isEqualTo(2);
		assertThat(tableParams.getFilter()).isEqualTo("z");
		assertThat(tableParams.getSortBy()).isEqualTo("name ASC");
		
		TableInfoDto tableInfo = result.getTableInfo();
		assertThat(tableInfo.getPageCount()).isEqualTo(2);
		assertThat(tableInfo.getRowCount()).isEqualTo(4);
		assertThat(tableInfo.getRowStart()).isEqualTo(3);
		assertThat(tableInfo.getRowEnd()).isEqualTo(4);
		
		List<EntryDto> rows = result.getRows();
		assertThat(rows).hasSize(2);
		assertThat(rows.get(0).getName()).isEqualTo("Zakupy");
		assertThat(rows.get(1).getName()).isEqualTo("Zus");
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
		Entry entry = entryHelper.save("Zakupy", bankAccount, "30.00");
		flush();
		
		// when
		EntryDto dto = new EntryDto(load(Entry.class, entry.getId()));
		dto.setAmount(new BigDecimal("35.00"));
		dto.setDescription("Nowy opis");
		EntryDto result = controller.update(entry.getId(), dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getDescription()).isEqualTo(dto.getDescription());
		assertThat(result.getAmount()).isEqualTo(dto.getAmount());
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
	
	@Test
	@Transactional
	public void updateBalances() {			
		// no test in H2 database
	}
	
}

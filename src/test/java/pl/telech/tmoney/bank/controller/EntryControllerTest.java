package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.bank.asserts.EntryAssert;
import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.model.shared.TableData.TableInfo;
import pl.telech.tmoney.utils.BaseMvcTest;

class EntryControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/entries";
		
	@Autowired
	AccountHelper accountHelper;
	
	@Autowired
	CategoryHelper categoryHelper;
	
	@Autowired
	EntryHelper entryHelper;
	
	@Autowired
	EntryMapper entryMapper;
	
	
	@Test
	void getById() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		Entry entry = entryHelper.save("Zakupy", account);
		
		// when
		EntryDto responseDto = get(baseUrl + "/" + entry.getId(), EntryDto.class);
		
		// then
		EntryAssert.assertThatDto(responseDto)
			.isMappedFrom(entry);
	}
	
	@Test
	void getTable() throws Exception {	
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
		
		// when
		String url = String.format(baseUrl + "/table/BANK?pageNo=%d&pageSize=%d&filter=%s&sortBy=%s", 1, 2, "z", "name ASC");
		TableData<EntryDto> result = get(url, new TypeReference<TableData<EntryDto>>() {});	
		
		// then
		assertThat(result).isNotNull();

		TableParams tableParams = result.getTableParams();
		assertThat(tableParams.getPageNo()).isEqualTo(1);
		assertThat(tableParams.getPageSize()).isEqualTo(2);
		assertThat(tableParams.getFilter()).isEqualTo("z");
		assertThat(tableParams.getSortBy()).isEqualTo("name ASC");
		
		TableInfo tableInfo = result.getTableInfo();
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
	void getTable_accountIsNull() throws Exception {	
		// given
		Account account1 = accountHelper.save("Konto bankowe", "BANK");
		Account account2 = accountHelper.save("Dom", "HOME");
		entryHelper.save("Zakupy", account2);
		entryHelper.save("Zus", account1);
		entryHelper.save("VAT-7", account1);
		entryHelper.save("Leasing", account1);
		entryHelper.save("OneDrive", account1);
		entryHelper.save("Benzyna", account2);
		entryHelper.save("Autostrada", account1);
		entryHelper.save("Czapka N", account2);
		
		// when
		String url = String.format(baseUrl + "/table?pageNo=%d&pageSize=%d&filter=%s&sortBy=%s", 1, 2, "z", "name ASC");
		TableData<EntryDto> result = get(url, new TypeReference<TableData<EntryDto>>() {});	
		
		// then
		assertThat(result).isNotNull();

		TableParams tableParams = result.getTableParams();
		assertThat(tableParams.getPageNo()).isEqualTo(1);
		assertThat(tableParams.getPageSize()).isEqualTo(2);
		assertThat(tableParams.getFilter()).isEqualTo("z");
		assertThat(tableParams.getSortBy()).isEqualTo("name ASC");
		
		TableInfo tableInfo = result.getTableInfo();
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
	void create() throws Exception {	
		// given
		Account bankAccount = accountHelper.save("Konto bankowe");
		Category category = categoryHelper.save("Zakupy");
		Entry entry = new EntryBuilder().name("Entry B2").account(bankAccount).category(category).amount(dec("30.00")).build();
		EntryDto requestDto = entryMapper.toDto(entry);
		
		// when	
		EntryDto responseDto = post(baseUrl, requestDto, EntryDto.class);	
		
		// then
		Entry createdEntry = dbHelper.load(Entry.class, responseDto.getId());
		EntryAssert.assertThatDto(responseDto)
			.isMappedFrom(createdEntry)
			.createdBy(requestDto);
	}
	
	@Test
	void update() throws Exception {	
		// given
		Account bankAccount = accountHelper.save("Konto bankowe");
		Entry entry = entryHelper.save("Zakupy", bankAccount, dec("30.00"));
		EntryDto requestDto = entryMapper.toDto(entry);
		requestDto.setAmount(dec("-30.00"));
		
		// when
		EntryDto responseDto = put(baseUrl + "/" + entry.getId(), requestDto, EntryDto.class);
		
		// then
		assertThat(responseDto.getId()).isEqualTo(entry.getId());
		Entry updatedEntry = dbHelper.load(Entry.class, responseDto.getId());
		EntryAssert.assertThatDto(responseDto)
			.isMappedFrom(updatedEntry)
			.updatedBy(requestDto);
	}
	
	@Test
	void delete() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		Entry entry = entryHelper.save("Zakupy", account);
		
		// when
		MvcResult result = delete(baseUrl + "/" + entry.getId());
		
		// then
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		
		Entry deletedEntry = dbHelper.load(Entry.class, entry.getId());
		assertThat(deletedEntry).isNull();
	}
	
	@Test
	void updateBalances() {	
		
	}
		
}

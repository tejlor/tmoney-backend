package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.bank.asserts.AccountAssert;
import pl.telech.tmoney.bank.asserts.EntryAssert;
import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.model.data.AccountSummaryData;
import pl.telech.tmoney.bank.model.data.BalanceRequest;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.helper.DBHelper;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.model.shared.TableData.TableInfo;
import pl.telech.tmoney.utils.BaseMvcTest;


class AccountControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/bank-accounts";
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	EntryHelper entryHelper;
	@Autowired
	DBHelper dbHelper;
	
	@Autowired
	AccountMapper accountMapper;
	
	@Test
	void getById() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		
		// when
		AccountDto responseDto = get(baseUrl + "/" + account.getId(), AccountDto.class);
		
		// then	
		AccountAssert.assertThatDto(responseDto)
			.isMappedFrom(account);
	}
	
	@Test
	void getByCode() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe", "BANK");
		
		// when
		AccountDto responseDto = get(baseUrl + "/" + account.getCode(), AccountDto.class);
		
		// then	
		AccountAssert.assertThatDto(responseDto)
			.isMappedFrom(account);
	}
	
	@Test
	void getByCode_summary() throws Exception {		
		// when
		AccountDto responseDto = get(baseUrl + "/SUMMARY", AccountDto.class);
		
		// then	
		assertThat(responseDto.getCode()).isEqualTo("SUMMARY");
		assertThat(responseDto.getName()).isEqualTo("Podsumowanie");
		assertThat(responseDto.getIcon()).isNotBlank();
		assertThat(responseDto.getLogo()).isNotBlank();
	}
	
	@Test
	void getAll() throws Exception {	
		// given
		Account account0 = accountHelper.save("Konto bankowe", true);	
		Account account1 = accountHelper.save("Dom", true);
		Account account2 = accountHelper.save("IKE", false);
		
		// when
		List<AccountDto> response = get(baseUrl, new TypeReference<List<AccountDto>>() {});
		
		// then	
		assertThat(response).hasSize(3);
		AccountAssert.assertThatDto(response.get(0)).isMappedFrom(account0);
		AccountAssert.assertThatDto(response.get(1)).isMappedFrom(account1);
		AccountAssert.assertThatDto(response.get(2)).isMappedFrom(account2);
	}
	
	@Test
	void getAll_active() throws Exception {	
		// given
		Account account0 = accountHelper.save("Konto bankowe", true);	
		Account account1 = accountHelper.save("Dom", true);
		accountHelper.save("IKE", false);
		
		// when
		String url = String.format(baseUrl + "?active=%s", true);
		List<AccountDto> response = get(url, new TypeReference<List<AccountDto>>() {});
		
		// then	
		assertThat(response).hasSize(2);
		AccountAssert.assertThatDto(response.get(0)).isMappedFrom(account0);
		AccountAssert.assertThatDto(response.get(1)).isMappedFrom(account1);
	}
	
	@Test
	void getTable() throws Exception {	
		// given
		accountHelper.save("Konto bankowe"); // 1
		accountHelper.save("Dom");
		accountHelper.save("Konto firmowe"); // 2
		accountHelper.save("Konto stare"); // 4
		accountHelper.save("Konto maklerskie"); // 3
		accountHelper.save("IKE");
		accountHelper.save("IKZE");
		
		// when
		String url = String.format(baseUrl + "/table?pageNo=%d&pageSize=%d&filter=%s&sortBy=%s", 1, 2, "kon", "name ASC");
		TableData<AccountDto> result = get(url, new TypeReference<TableData<AccountDto>>() {});	
		
		// then
		assertThat(result).isNotNull();

		TableParams tableParams = result.getTableParams();
		assertThat(tableParams.getPageNo()).isEqualTo(1);
		assertThat(tableParams.getPageSize()).isEqualTo(2);
		assertThat(tableParams.getFilter()).isEqualTo("kon");
		assertThat(tableParams.getSortBy()).isEqualTo("name ASC");
		
		TableInfo tableInfo = result.getTableInfo();
		assertThat(tableInfo.getPageCount()).isEqualTo(2);
		assertThat(tableInfo.getRowCount()).isEqualTo(4);
		assertThat(tableInfo.getRowStart()).isEqualTo(3);
		assertThat(tableInfo.getRowEnd()).isEqualTo(4);
		
		List<AccountDto> rows = result.getRows();
		assertThat(rows).hasSize(2);
		assertThat(rows.get(0).getName()).isEqualTo("Konto maklerskie");
		assertThat(rows.get(1).getName()).isEqualTo("Konto stare");
	}
	
	@Test
	void create() throws Exception {	
		// given
		Account account = new AccountBuilder().name("Konto bankowe").build();
		AccountDto requestDto = accountMapper.toDto(account);
		
		// when
		AccountDto responseDto = post(baseUrl, requestDto, AccountDto.class);
		
		// then	
		Account createdAccount = dbHelper.load(Account.class, responseDto.getId());	
		AccountAssert.assertThatDto(responseDto)
			.isMappedFrom(createdAccount)
			.createdBy(requestDto);
	}
	
	@Test
	void update() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe");
		AccountDto requestDto = accountMapper.toDto(account);
		requestDto.setActive(false);
		
		// when
		AccountDto responseDto = put(baseUrl + "/" + account.getId(), requestDto, AccountDto.class);
		
		// then	
		assertThat(responseDto.getId()).isEqualTo(account.getId());
		Account updatedAccount = dbHelper.load(Account.class, responseDto.getId());	
		AccountAssert.assertThatDto(responseDto)
			.isMappedFrom(updatedAccount)
			.updatedBy(requestDto);
	}
	
	@Test
	void getSummary() throws Exception {
		// given
		Account account0 = accountHelper.save("Konto bankowe", "BANK");
		Account account1 = accountHelper.save("Dom", "HOME");		
		entryHelper.save("Zakupy", date("2020-05-12"), account0);
		entryHelper.save("Fryzjer", date("2020-07-12"), account1);
		Entry entry0 = entryHelper.save("ZUS", date("2020-05-13"), account0);
		Entry entry1 = entryHelper.save("Zakupy", date("2020-07-13"), account1);
		
		// when
		List<AccountSummaryData> result = get(baseUrl + "/summary", new TypeReference<List<AccountSummaryData>>() {});
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		
		AccountAssert.assertThatDto(result.get(0).getAccount()).isMappedFrom(account0);
		EntryAssert.assertThatDto(result.get(0).getEntry()).isMappedFrom(entry0);
		
		AccountAssert.assertThatDto(result.get(1).getAccount()).isMappedFrom(account1);
		EntryAssert.assertThatDto(result.get(1).getEntry()).isMappedFrom(entry1);
		
		assertThat(result.get(2).getAccount().getCode().equals("SUMMARY"));
		EntryAssert.assertThatDto(result.get(2).getEntry()).isMappedFrom(entry1);
	}
	
	@Test
	void getSummary_byCode() throws Exception {
		// given
		Account account0 = accountHelper.save("Konto bankowe", "BANK");
		Account account1 = accountHelper.save("Dom", "HOME");		
		entryHelper.save("Zakupy", date("2020-05-12"), account0);
		entryHelper.save("Fryzjer", date("2020-07-12"), account1);
		Entry entry0 = entryHelper.save("ZUS", date("2020-05-13"), account0);
		entryHelper.save("Zakupy", date("2020-07-13"), account1);
		
		// when
		List<AccountSummaryData> result = get(baseUrl + "/summary/BANK", new TypeReference<List<AccountSummaryData>>() {});
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		AccountAssert.assertThatDto(result.get(0).getAccount()).isMappedFrom(account0);
		EntryAssert.assertThatDto(result.get(0).getEntry()).isMappedFrom(entry0);
	}
	
	@Test
	void balance() throws Exception {
		// given
		Category category = categoryHelper.save("Liczenie");
		Account account = accountHelper.save("Dom", category);
		Entry lastEntry = entryHelper.save("Zakupy", account, category, date("2022-02-15"), dec("2 546,89"));
		BalanceRequest request = new BalanceRequest(account.getId(), date("2022-03-01"), dec("2 520,13"));
		
		// when
		post(baseUrl + "/" + account.getId() + "/balance", request);
		
		// then
		Optional<Entry> optionalEntry = dbHelper.loadAll(Entry.class).stream().filter(e -> !e.equals(lastEntry)).findAny();
		assertThat(optionalEntry).isNotEmpty();
		
		Entry balancingEntry = optionalEntry.get();
		assertThat(balancingEntry.getAccount()).isEqualTo(account);
		assertThat(balancingEntry.getCategory()).isEqualTo(category);
		assertThat(balancingEntry.getDate()).isEqualTo(request.getDate());
		assertThat(balancingEntry.getAmount()).isEqualTo(dec("-26,76"));
		assertThat(balancingEntry.getName()).isEqualTo(category.getDefaultName());
		assertThat(balancingEntry.getDescription()).isEqualTo(category.getDefaultDescription());
	}
}

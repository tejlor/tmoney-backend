package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.bank.asserts.AccountAssert;
import pl.telech.tmoney.bank.asserts.EntryAssert;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.mapper.AccountMapper;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.dto.AccountSummaryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.utils.BaseMvcTest;


class AccountControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/bank-accounts";
	
	@Autowired
	AccountHelper accountHelper;
	
	@Autowired
	EntryHelper entryHelper;
	
	@Autowired
	AccountMapper accountMapper;
	
	@Test
	void getByCode() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe", "bank1");
		
		// when
		AccountDto responseDto = get(baseUrl + "/" + account.getCode(), AccountDto.class);
		
		// then	
		AccountAssert.assertThatDto(responseDto)
			.isMappedFrom(account);
	}
	
	@Test
	void getActive() throws Exception {	
		// given
		Account account0 = accountHelper.save("Konto bankowe", true);	
		Account account1 = accountHelper.save("Dom", true);
		accountHelper.save("IKE", false);
		
		// when
		List<AccountDto> response = get(baseUrl, new TypeReference<List<AccountDto>>() {});
		
		// then	
		assertThat(response).hasSize(2);
		AccountAssert.assertThatDto(response.get(0)).isMappedFrom(account0);
		AccountAssert.assertThatDto(response.get(1)).isMappedFrom(account1);
	}
	
	@Test
	void create() throws Exception {	
		// given
		Account account = accountHelper.build("Konto bankowe");
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
	public void getSummary() throws Exception {
		// given
		Account account0 = accountHelper.save("Konto bankowe", "BANK");
		Account account1 = accountHelper.save("Dom", "HOME");		
		entryHelper.save("Zakupy", date("2020-05-12"), account0);
		entryHelper.save("Fryzjer", date("2020-07-12"), account1);
		Entry entry0 = entryHelper.save("ZUS", date("2020-05-13"), account0);
		Entry entry1 = entryHelper.save("Zakupy", date("2020-07-13"), account1);
		
		// when
		List<AccountSummaryDto> result = get(baseUrl + "/summary", new TypeReference<List<AccountSummaryDto>>() {});
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		AccountAssert.assertThatDto(result.get(0).getAccount()).isMappedFrom(account0);
		AccountAssert.assertThatDto(result.get(1).getAccount()).isMappedFrom(account1);
		EntryAssert.assertThatDto(result.get(0).getEntry()).isMappedFrom(entry0);
		EntryAssert.assertThatDto(result.get(1).getEntry()).isMappedFrom(entry1);
	}
	
	@Test
	public void getSummary_byCode() throws Exception {
		// given
		Account account0 = accountHelper.save("Konto bankowe", "BANK");
		Account account1 = accountHelper.save("Dom", "HOME");		
		entryHelper.save("Zakupy", date("2020-05-12"), account0);
		entryHelper.save("Fryzjer", date("2020-07-12"), account1);
		Entry entry0 = entryHelper.save("ZUS", date("2020-05-13"), account0);
		entryHelper.save("Zakupy", date("2020-07-13"), account1);
		
		// when
		List<AccountSummaryDto> result = get(baseUrl + "/summary/BANK", new TypeReference<List<AccountSummaryDto>>() {});
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		AccountAssert.assertThatDto(result.get(0).getAccount()).isMappedFrom(account0);
		EntryAssert.assertThatDto(result.get(0).getEntry()).isMappedFrom(entry0);
	}
}

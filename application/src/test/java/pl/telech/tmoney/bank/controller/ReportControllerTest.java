package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.utils.BaseMvcTest;


class ReportControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/reports";
	
	@Autowired
	AccountHelper accountHelper;
	
	@Autowired
	EntryHelper entryHelper;
	
	
	@Test
	void generateTable() throws Exception {	
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
		MvcResult result = get2(baseUrl + "/table/BANK");
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(30000);
		
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_DISPOSITION)).isEqualTo("attachment; filename=\"Konto bankowe.pdf\"");
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
	}
	
	@Test
	void generateTable_Summary() throws Exception {	
		// given
		Account account1 = accountHelper.save("Konto bankowe", "BANK");
		Account account2 = accountHelper.save("Dom", "HOME");
		entryHelper.save("Zakupy", account2);
		entryHelper.save("Zus", account1);
		entryHelper.save("VAT-7", account1);
		entryHelper.save("Leasing", account1);
		entryHelper.save("OneDrive", account1);
		entryHelper.save("Benzyna", account2);
		entryHelper.save("Autostrada", account2);
		entryHelper.save("Czapka N", account1);
		
		
		// when
		MvcResult result = get2(baseUrl + "/table");
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(30000);
		
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_DISPOSITION)).isEqualTo("attachment; filename=\"Podsumowanie.pdf\"");
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
	}
	
	@Test
	void generateReport() throws Exception {
		// given
		Account accountBank = accountHelper.save("Konto bankowe", "BANK");
		Account accountHome = accountHelper.save("Dom", "HOME");
		entryHelper.save("Zakupy", date("2022-12-01"), accountHome);
		entryHelper.save("Zus", date("2022-01-01"), accountBank);
		entryHelper.save("VAT-7", date("2022-01-01"), accountBank);
		entryHelper.save("Leasing", date("2022-01-01"), accountBank);
		entryHelper.save("OneDrive", date("2022-01-01"), accountBank);
		entryHelper.save("Benzyna", date("2022-01-01"), accountHome);
		entryHelper.save("Autostrada", date("2022-01-01"), accountHome);
		entryHelper.save("Czapka N", date("2022-01-01"), accountBank);		
		
		// when
		MvcResult result = get2(baseUrl + "/report?dateFrom=2022-01-01&dateTo=2025-12-31");
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(30000);
		
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_DISPOSITION)).isEqualTo("attachment; filename=\"Raport 2022.pdf\"");
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
	}
	
}

package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.utils.BaseMvcTest;


public class ReportControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/reports";
	
	@Autowired
	AccountHelper accountHelper;
	
	@Autowired
	EntryHelper entryHelper;
	
	
	@Test
	void generateTable() throws Exception {	
		// given
		Account account = accountHelper.save("Konto bankowe", "SANTANDER");
		entryHelper.save("Zakupy", account);
		entryHelper.save("Zus", account);
		entryHelper.save("VAT-7", account);
		entryHelper.save("Leasing", account);
		entryHelper.save("OneDrive", account);
		entryHelper.save("Benzyna", account);
		entryHelper.save("Autostrada", account);
		entryHelper.save("Czapka N", account);
		
		// when
		MvcResult result = get2(baseUrl + "/table/SANTANDER");
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(30000);
		
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_DISPOSITION)).isEqualTo("attachment; filename=\"Konto bankowe.pdf\"");
		assertThat(result.getResponse().getHeaderValue(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
	}
	
	@Test
	void generateTable_Summary() throws Exception {	
		// given
		Account account1 = accountHelper.save("Konto bankowe", "SANTANDER");
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
	
}

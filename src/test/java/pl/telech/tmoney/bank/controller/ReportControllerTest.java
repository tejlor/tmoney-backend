package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class ReportControllerTest extends BaseTest {

	@Autowired
	ReportController controller;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	EntryHelper entryHelper;
	
	
	@Test
	@Transactional
	public void generateTable() {	
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
		flush();
		
		
		// when
		ResponseEntity<byte[]> result = controller.generateTable("SANTANDER");	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		
		byte[] body = result.getBody();
		assertThat(body).isNotNull();
		assertThat(body.length).isGreaterThan(30000);
		
		HttpHeaders headers = result.getHeaders();
		assertThat(headers.get(HttpHeaders.CONTENT_DISPOSITION).get(0)).isEqualTo("attachment; filename=\"Konto bankowe.pdf\"");
		assertThat(headers.get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
	}
	
	@Test
	@Transactional
	public void generateTable_Summary() {	
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
		flush();
		
		
		// when
		ResponseEntity<byte[]> result = controller.generateTable(null);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		
		byte[] body = result.getBody();
		assertThat(body).isNotNull();
		assertThat(body.length).isGreaterThan(30000);
		
		HttpHeaders headers = result.getHeaders();
		assertThat(headers.get(HttpHeaders.CONTENT_DISPOSITION).get(0)).isEqualTo("attachment; filename=\"Podsumowanie.pdf\"");
		assertThat(headers.get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
	}
	

	
}

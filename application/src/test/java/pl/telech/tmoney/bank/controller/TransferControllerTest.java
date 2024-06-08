package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.date;
import static pl.telech.tmoney.utils.TestUtils.dec;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.telech.tmoney.bank.helper.TransferDefinitionHelper;
import pl.telech.tmoney.bank.model.data.TransferRequest;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.helper.DBHelper;
import pl.telech.tmoney.utils.BaseMvcTest;


class TransferControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/transfers";
	
	@Autowired
	TransferDefinitionHelper transferDefinitionHelper;
	@Autowired
	DBHelper dbHelper;

	
	@Test
	void createTransfer() throws Exception {
		// given
		TransferDefinition definition = transferDefinitionHelper.save("Wyplata z bankomatu");
		TransferRequest transfer = new TransferRequest(definition.getId(), date("2022-05-19"), dec("2 000.00"), "Wypłata z bankomtu", "Auchan");
		
		// when
		postResult(baseUrl, transfer);
		
		// then
		List<Entry> entries = dbHelper.loadAll(Entry.class);
		assertThat(entries).hasSize(2);
		
		Entry sourceEntry = entries.get(0);
		assertThat(sourceEntry.getAccount()).isEqualTo(definition.getSourceAccount());
		assertThat(sourceEntry.getCategory()).isEqualTo(definition.getCategory());
		assertThat(sourceEntry.getAmount()).isEqualTo(transfer.getAmount().negate());
		assertThat(sourceEntry.getDate()).isEqualTo(transfer.getDate());
		assertThat(sourceEntry.getName()).isEqualTo(transfer.getName());
		assertThat(sourceEntry.getDescription()).isEqualTo(transfer.getDescription());
		
		Entry destEntry = entries.get(1);
		assertThat(destEntry.getAccount()).isEqualTo(definition.getDestinationAccount());
		assertThat(destEntry.getCategory()).isEqualTo(definition.getCategory());
		assertThat(destEntry.getAmount()).isEqualTo(transfer.getAmount());
		assertThat(destEntry.getDate()).isEqualTo(transfer.getDate());
		assertThat(destEntry.getName()).isEqualTo(transfer.getName());
		assertThat(destEntry.getDescription()).isEqualTo(transfer.getDescription());
	}
}

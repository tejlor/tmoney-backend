package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.bank.asserts.TransferDefinitionAssert;
import pl.telech.tmoney.bank.builder.TransferDefinitionBuilder;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.TransferDefinitionHelper;
import pl.telech.tmoney.bank.mapper.TransferDefinitionMapper;
import pl.telech.tmoney.bank.model.data.TransferRequest;
import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.helper.DBHelper;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.model.shared.TableData.TableInfo;
import pl.telech.tmoney.utils.BaseMvcTest;


class TransferControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/transfers";
	
	@Autowired
	TransferDefinitionHelper transferDefinitionHelper;
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	DBHelper dbHelper;
	
	@Autowired
	TransferDefinitionMapper accountMapper;
	
	@Test
	void getById() throws Exception {	
		// given
		TransferDefinition definition = transferDefinitionHelper.save("Wypłata z bankomatu");
		
		// when
		TransferDefinitionDto responseDto = get(baseUrl + "/definition/" + definition.getId(), TransferDefinitionDto.class);
		
		// then	
		TransferDefinitionAssert.assertThatDto(responseDto)
			.isMappedFrom(definition);
	}
	
	@Test
	void getTable() throws Exception {	
		// given
		transferDefinitionHelper.save("Wypłata z bankomatu");
		transferDefinitionHelper.save("Przelew na osobiste");
		transferDefinitionHelper.save("Przelew na firmowe");
		transferDefinitionHelper.save("Zasilenie IKE"); // 2
		transferDefinitionHelper.save("Zasilenie IKZE"); // 3
		transferDefinitionHelper.save("Zasilenie Giełdy"); // 1
		
		
		// when
		String url = String.format(baseUrl + "/definition/table?pageNo=%d&pageSize=%d&filter=%s&sortBy=%s", 1, 2, "zasil", "name ASC");
		TableData<TransferDefinitionDto> result = get(url, new TypeReference<TableData<TransferDefinitionDto>>() {});	
		
		// then
		assertThat(result).isNotNull();

		TableParams tableParams = result.getTableParams();
		assertThat(tableParams.getPageNo()).isEqualTo(1);
		assertThat(tableParams.getPageSize()).isEqualTo(2);
		assertThat(tableParams.getFilter()).isEqualTo("zasil");
		assertThat(tableParams.getSortBy()).isEqualTo("name ASC");
		
		TableInfo tableInfo = result.getTableInfo();
		assertThat(tableInfo.getPageCount()).isEqualTo(2);
		assertThat(tableInfo.getRowCount()).isEqualTo(3);
		assertThat(tableInfo.getRowStart()).isEqualTo(3);
		assertThat(tableInfo.getRowEnd()).isEqualTo(3);
		
		List<TransferDefinitionDto> rows = result.getRows();
		assertThat(rows).hasSize(1);
		assertThat(rows.get(0).getName()).isEqualTo("Zasilenie IKZE");
	}
	
	@Test
	void create() throws Exception {	
		// given
		Account bankAccount = accountHelper.save("Konto bankowe");
		Account homeAccount = accountHelper.save("Dom");
		Category category = categoryHelper.save("Wypłata");
		TransferDefinition definition = new TransferDefinitionBuilder().sourceAccount(bankAccount).destinationAccount(homeAccount).category(category).name("Wyplata z bankomatu").build();
		TransferDefinitionDto requestDto = accountMapper.toDto(definition);
		
		// when
		TransferDefinitionDto responseDto = post(baseUrl + "/definition", requestDto, TransferDefinitionDto.class);
		
		// then	
		TransferDefinition createdDefinition = dbHelper.load(TransferDefinition.class, responseDto.getId());	
		TransferDefinitionAssert.assertThatDto(responseDto)
			.isMappedFrom(createdDefinition)
			.createdBy(requestDto);
	}
	
	@Test
	void update() throws Exception {	
		// given
		TransferDefinition definition = transferDefinitionHelper.save("Wyplata z bankomatu");
		TransferDefinitionDto requestDto = accountMapper.toDto(definition);
		requestDto.setDescription("inny opis");
		
		// when
		TransferDefinitionDto responseDto = put(baseUrl + "/definition/" + definition.getId(), requestDto, TransferDefinitionDto.class);
		
		// then	
		assertThat(responseDto.getId()).isEqualTo(definition.getId());
		TransferDefinition updatedDefinition = dbHelper.load(TransferDefinition.class, responseDto.getId());	
		TransferDefinitionAssert.assertThatDto(responseDto)
			.isMappedFrom(updatedDefinition)
			.updatedBy(requestDto);
	}
	
	@Test
	void delete() throws Exception {	
		// given
		TransferDefinition definition = transferDefinitionHelper.save("Wyplata z bankomatu");
		
		// when
		MvcResult result = delete(baseUrl + "/definition/" + definition.getId());
		
		// then
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		
		TransferDefinition deletedDefinition = dbHelper.load(TransferDefinition.class, definition.getId());
		assertThat(deletedDefinition).isNull();
	}
	
	@Test
	void createTransfer() throws Exception {
		// given
		TransferDefinition definition = transferDefinitionHelper.save("Wyplata z bankomatu");
		TransferRequest transfer = new TransferRequest(definition.getId(), date("2022-05-19"), dec("2 000.00"), "Wypłata z bankomtu", "Auchan");
		
		// when
		post(baseUrl, transfer);
		
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

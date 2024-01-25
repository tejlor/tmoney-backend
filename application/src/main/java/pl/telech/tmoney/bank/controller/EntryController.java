package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pl.telech.processor.annotation.AutoMethod;
import pl.telech.processor.annotation.enums.Type;
import pl.telech.tmoney.bank.logic.BankLogic;
import pl.telech.tmoney.bank.logic.EntryLogic;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entries")
public class EntryController extends AbstractController {

	final EntryMapper mapper;
	final EntryLogic entryLogic;
	final BankLogic bankLogic;
		
	@AutoMethod(type = Type.GET_BY_ID)
	@AutoMethod(type = Type.CREATE)
	@AutoMethod(type = Type.UPDATE)
	@AutoMethod(type = Type.DELETE)
	void init() {}

	/*
	 * Returns children of element for table.
	 */
	@RequestMapping(value = {"/table", "/table/{accountCode:" + CODE + "}"}, method = GET)
	public TableData<EntryDto> getTable(
		@PathVariable(required = false) String accountCode,
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy){
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<Entry>, Integer> result = entryLogic.loadAll(accountCode, tableParams); 	
		var table = new TableData<EntryDto>(tableParams);
		table.setRows(mapper.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Parse transactions file
	 */
	@RequestMapping(value = "/transactions", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public List<EntryDto> parseTransactions(
			@RequestPart @NotNull MultipartFile file) {
		
		return mapper.toDtoList(bankLogic.parseTransactionsFile(file));
	}
	
	/*
	 * Updates all balances.
	 */
	@RequestMapping(value = "/updateBalances", method = POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateBalances() {
		
		entryLogic.updateBalances();
	}
}

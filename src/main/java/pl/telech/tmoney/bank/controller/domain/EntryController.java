package pl.telech.tmoney.bank.controller.domain;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.logic.EntryLogic;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entries")
public class EntryController extends AbstractDomainController<Entry, EntryDto> {

	final EntryMapper mapper;
	final EntryLogic entryLogic;
		
	
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
	 * Deletes entry.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
			@PathVariable int id) {
		
		entryLogic.delete(id);
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

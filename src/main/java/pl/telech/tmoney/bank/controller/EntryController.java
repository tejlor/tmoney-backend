package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.dto.TableDataDto;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequestMapping("/entries")
@FieldDefaults(level = PRIVATE)
public class EntryController extends AbstractController {

	@Autowired
	EntryLogic entryLogic;
		
	/*
	 * Returns children of element for table.
	 */
	@RequestMapping(value = "table/{code:" + CODE + "}", method = GET)
	public TableDataDto<EntryDto> getAll(
		@PathVariable(name = "code") String accountCode,
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy){
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<Entry>, Integer> result = entryLogic.loadAll(accountCode, tableParams); 	
		var table = new TableDataDto<EntryDto>(tableParams);
		table.setRows(EntryDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Returns entry by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public EntryDto getById(
			@PathVariable int id) {
		
		return new EntryDto(entryLogic.loadById(id));
	}
	
	/*
	 * Creates new entry.
	 */
	@RequestMapping(value = "", method = POST)
	public EntryDto create(
			@RequestBody EntryDto entry) {
		
		return new EntryDto(entryLogic.create(entry.toModel()));
	}
	
	/*
	 * Updates entry.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public EntryDto update(
			@PathVariable int id,
			@RequestBody EntryDto entry) {
		
		TUtils.assertDtoId(id, entry);
		return new EntryDto(entryLogic.update(id, entry.toModel()));
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

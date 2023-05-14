package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.logic.BankLogic;
import pl.telech.tmoney.bank.logic.TransferDefinitionLogic;
import pl.telech.tmoney.bank.mapper.TransferDefinitionMapper;
import pl.telech.tmoney.bank.model.data.TransferRequest;
import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfers")
public class TransferController extends AbstractController {

	final TransferDefinitionMapper mapper;
	final TransferDefinitionLogic transferDefinitionLogic;
	final BankLogic bankLogic;
	
	
	/*
	 * Returns account by id.
	 */
	@RequestMapping(value = "/definition/{id:" + ID + "}", method = GET)
	public TransferDefinitionDto getById(
			@PathVariable int id) {
		
		return mapper.toDto(transferDefinitionLogic.loadById(id));
	}
	
	/*
	 * Returns all accounts for table.
	 */
	@RequestMapping(value = "/definition/table", method = GET)
	public TableData<TransferDefinitionDto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy) {
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<TransferDefinition>, Integer> result = transferDefinitionLogic.loadTable(tableParams); 	
		var table = new TableData<TransferDefinitionDto>(tableParams);
		table.setRows(mapper.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	

	/*
	 * Creates new account.
	 */
	@RequestMapping(value = "/definition", method = POST)
	public TransferDefinitionDto create(
			@RequestBody @Valid TransferDefinitionDto account) {
		
		return mapper.toDto(transferDefinitionLogic.create(account));
	}
	
	/*
	 * Updates account.
	 */
	@RequestMapping(value = "/definition/{id:" + ID + "}", method = PUT)
	public TransferDefinitionDto update(
			@PathVariable int id,
			@RequestBody @Valid TransferDefinitionDto account) {
		
		TUtils.assertDtoId(id, account);
		return mapper.toDto(transferDefinitionLogic.update(id, account));
	}
	
	/*
	 * Deletes definition.
	 */
	@RequestMapping(value = "/definition/{id:" + ID + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
			@PathVariable int id) {
		
		transferDefinitionLogic.delete(id);
	}
	
	/*
	 * Creates transfer.
	 */
	@RequestMapping(value = "", method = POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void createTransfer(
			@RequestBody @Valid TransferRequest transfer) {
		
		bankLogic.createTransfer(transfer);
	}
	
}

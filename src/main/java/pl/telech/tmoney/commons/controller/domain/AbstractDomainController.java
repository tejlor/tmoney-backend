package pl.telech.tmoney.commons.controller.domain;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.commons.logic.AbstractDomainLogic;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractDomainController<E extends AbstractEntity, T extends AbstractDto> {
	
	protected static final String ID = "[0-9]+";
	protected static final String CODE = "[A-Z_]+";
	
	@Autowired
	EntityMapper<E,T> mapper;
	
	@Autowired
	AbstractDomainLogic<E,T> logic;
	
	
	/*
	 * Returns object by id.
	 */
	@GetMapping(value = "/{id:" + ID + "}")
	public T getById(@PathVariable int id) {	
		return mapper.toDto(logic.loadById(id));
	}
	
	/*
	 * Returns all objects.
	 */
	@GetMapping(value = "")
	public List<T> getAll() {	
		return mapper.toDtoList(logic.loadAll());
	}
	
	/*
	 * Returns all objects for table.
	 */
	@GetMapping(value = "/table")
	public TableData<T> getTable(@RequestParam(required = false) Integer pageNo,
								 @RequestParam(required = false) Integer pageSize,
								 @RequestParam(required = false) String filter,
								 @RequestParam(required = false) String sortBy) {
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<E>, Integer> result = logic.loadTable(tableParams); 	
		var table = new TableData<T>(tableParams);
		table.setRows(mapper.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Creates new object.
	 */
	@PostMapping(value = "")
	public T create(@RequestBody @Valid T dto) {		
		return mapper.toDto(logic.create(dto));
	}
	
	/*
	 * Updates object.
	 */
	@PutMapping(value = "/{id:" + ID + "}")
	public T update(@PathVariable int id, @RequestBody @Valid T dto) {		
		TUtils.assertDtoId(id, dto);
		return mapper.toDto(logic.update(id, dto));
	}
	
}

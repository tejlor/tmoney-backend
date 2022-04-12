package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.utils.TUtils;
import pl.telech.tmoney.main.logic.interfaces.EntryLogic;
import pl.telech.tmoney.main.model.dto.EntryDto;

@RestController
@RequestMapping("/entries")
@FieldDefaults(level = PRIVATE)
public class EntryController extends AbstractController {

	@Autowired
	EntryLogic entryLogic;
	
	/*
	 * Returns all entries.
	 */
	@RequestMapping(value = "", method = GET)
	public List<EntryDto> getAll() {
		
		return EntryDto.toDtoList(entryLogic.loadAll());
	}
	
	/*
	 * Returns entry by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public EntryDto getById(int id) {
		
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
}

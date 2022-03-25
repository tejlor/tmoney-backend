package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.main.logic.interfaces.EntryLogic;
import pl.telech.tmoney.main.model.dto.EntryDto;

@RestController
@RequestMapping("/entries")
@FieldDefaults(level = PRIVATE)
public class EntryController extends AbstractController {

	@Autowired
	EntryLogic entryLogic;
	
	/*
	 * Returns entry by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public EntryDto getById(int id) {
		
		return new EntryDto(entryLogic.loadById(id));
	}
}

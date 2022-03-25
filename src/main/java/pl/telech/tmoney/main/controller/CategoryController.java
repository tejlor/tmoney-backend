package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.main.logic.interfaces.CategoryLogic;
import pl.telech.tmoney.main.model.dto.CategoryDto;

@RestController
@RequestMapping("/categories")
@FieldDefaults(level = PRIVATE)
public class CategoryController extends AbstractController {

	@Autowired
	CategoryLogic categoryLogic;
	
	/*
	 * Returns category by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public CategoryDto getById(int id) {
		
		return new CategoryDto(categoryLogic.loadById(id));
	}
}

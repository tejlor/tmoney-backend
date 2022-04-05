package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.utils.TUtils;
import pl.telech.tmoney.main.logic.interfaces.CategoryLogic;
import pl.telech.tmoney.main.model.dto.CategoryDto;

@RestController
@RequestMapping("/categories")
@FieldDefaults(level = PRIVATE)
public class CategoryController extends AbstractController {

	@Autowired
	CategoryLogic categoryLogic;
	
	/*
	 * Returns all categories.
	 */
	@RequestMapping(value = "", method = GET)
	public List<CategoryDto> getAll() {
		
		return CategoryDto.toDtoList(categoryLogic.loadAll());
	}
	
	/*
	 * Returns category by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public CategoryDto getById(int id) {
		
		return new CategoryDto(categoryLogic.loadById(id));
	}
	
	/*
	 * Creates new category.
	 */
	@RequestMapping(value = "", method = POST)
	public CategoryDto create(
			@RequestBody CategoryDto category) {
		
		return new CategoryDto(categoryLogic.create(category.toModel()));
	}
	
	/*
	 * Updates category.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public CategoryDto update(
			@PathVariable int id,
			@RequestBody CategoryDto category) {
		
		TUtils.assertDtoId(id, category);
		return new CategoryDto(categoryLogic.update(id, category.toModel()));
	}
}

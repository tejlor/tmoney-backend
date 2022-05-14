package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.logic.interfaces.CategoryLogic;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.utils.TUtils;

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
	 * Returns by account id.
	 */
	@RequestMapping(value = "/{code:" + CODE + "}", method = GET)
	public List<CategoryDto> getByAccountCode(
			@PathVariable String code) {
		
		return sort(CategoryDto.toDtoList(categoryLogic.loadByAccountCode(code)));
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
	
	/*
	 * Deletes category.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
			@PathVariable int id) {
		
		categoryLogic.delete(id);
	}
}

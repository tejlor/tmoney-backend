package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import pl.telech.tmoney.bank.logic.CategoryLogic;
import pl.telech.tmoney.bank.mapper.CategoryMapper;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.dto.TableDataDto;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

	@Autowired
	CategoryMapper mapper;
	
	@Autowired
	CategoryLogic categoryLogic;
	
	/*
	 * Returns all categories for table.
	 */
	@RequestMapping(value = "/table", method = GET)
	public TableDataDto<CategoryDto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy) {
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<Category>, Integer> result = categoryLogic.loadTable(tableParams); 	
		var table = new TableDataDto<CategoryDto>(tableParams);
		table.setRows(mapper.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Returns all categories.
	 */
	@RequestMapping(value = "", method = GET)
	public List<CategoryDto> getAll() {
		
		return sort(mapper.toDtoList(categoryLogic.loadAll()));
	}
	
	/*
	 * Returns category by id.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public CategoryDto getById(
			@PathVariable int id) {
		
		return mapper.toDto(categoryLogic.loadById(id));
	}
	
	/*
	 * Returns category by account code.
	 */
	@RequestMapping(value = "/account/{code:" + CODE + "}", method = GET)
	public List<CategoryDto> getByAccountCode(
			@PathVariable String code) {
		
		return sort(mapper.toDtoList(categoryLogic.loadByAccountCode(code)));
	}
	
	/*
	 * Creates new category.
	 */
	@RequestMapping(value = "", method = POST)
	public CategoryDto create(
			@RequestBody CategoryDto category) {
		
		return mapper.toDto(categoryLogic.create(category));
	}
	
	/*
	 * Updates category.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public CategoryDto update(
			@PathVariable int id,
			@RequestBody CategoryDto category) {
		
		TUtils.assertDtoId(id, category);
		return mapper.toDto(categoryLogic.update(id, category));
	}
	
	/*
	 * Deletes category.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
			@PathVariable int id,
			@RequestParam(required = false) Integer newCategoryId) {
		
		categoryLogic.delete(id, newCategoryId);
	}
}

package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pl.telech.processor.annotation.AutoMethod;
import pl.telech.processor.annotation.enums.Type;
import pl.telech.tmoney.bank.logic.CategoryLogic;
import pl.telech.tmoney.bank.mapper.CategoryMapper;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.commons.controller.AbstractController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

	final CategoryMapper mapper;
	final CategoryLogic logic;
	
	@AutoMethod(type = Type.GET_BY_ID)
	@AutoMethod(type = Type.GET_TABLE)
	@AutoMethod(type = Type.GET_ALL)
	@AutoMethod(type = Type.UPDATE)
	@AutoMethod(type = Type.CREATE)
	private void init() {}
		
	/*
	 * Returns category by account code.
	 */
	@RequestMapping(value = "/account/{code:" + CODE + "}", method = GET)
	public List<CategoryDto> getByAccountCode(
			@PathVariable String code) {
		
		return mapper.toDtoList(logic.loadByAccountCode(code));
	}
	
	/*
	 * Deletes category.
	 */
	@RequestMapping(value = "/{id:" + ID + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
			@PathVariable int id,
			@RequestParam(required = false) Integer newCategoryId) {
		
		logic.delete(id, newCategoryId);
	}
}

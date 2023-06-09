package pl.telech.tmoney.bank.controller.domain;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.logic.CategoryLogic;
import pl.telech.tmoney.bank.mapper.CategoryMapper;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController extends AbstractDomainController<Category, CategoryDto> {

	final CategoryMapper mapper;
	final CategoryLogic categoryLogic;
	
	/*
	 * Returns category by account code.
	 */
	@RequestMapping(value = "/account/{code:" + CODE + "}", method = GET)
	public List<CategoryDto> getByAccountCode(
			@PathVariable String code) {
		
		return mapper.toDtoList(categoryLogic.loadByAccountCode(code));
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

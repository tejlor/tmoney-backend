package pl.telech.tmoney.bank.logic;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.CategoryDAO;
import pl.telech.tmoney.bank.logic.validator.CategoryValidator;
import pl.telech.tmoney.bank.mapper.CategoryMapper;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogic;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.exception.ValidationException;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryLogic extends AbstractLogic<Category> {
	
	final CategoryDAO dao;
	final CategoryMapper mapper;
	final AccountLogic accountLogic;
	final EntryLogic entryLogic;
	final CategoryValidator validator;
	
	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
	}
	
	public Pair<List<Category>, Integer> loadTable(TableParams params) {
		return dao.findTable(params);
	}
	
	public List<Category> loadByAccountCode(String accountCode) {
		Account account = accountLogic.loadByCode(accountCode);
		return loadAll().stream()
			.filter(a -> (a.getAccount() & 1 << account.getId()) != 0)
			.collect(Collectors.toList());
	}
		
	public Category create(CategoryDto categoryDto) {		
		Category newCategory = mapper.create(categoryDto);
		
		Errors errors = new BeanPropertyBindingResult(newCategory, "Konto");
		validator.validate(newCategory, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(newCategory);
	}
	
	public Category update(int id, CategoryDto categoryDto) {
		Category category = loadById(id);
		mapper.update(category, categoryDto);
		
		Errors errors = new BeanPropertyBindingResult(category, "Konto");
		validator.validate(category, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(category);
	}
	
	public void delete(int id, Integer newCategoryId) {
		Category category = loadById(id);
		TUtils.assertEntityExists(category);
		
		List<Entry> entries = entryLogic.loadByCategoryId(id);
		if(!entries.isEmpty()) {
			if(newCategoryId != null) {
				dao.changeCategory(id,  newCategoryId);
			}
			else {
				var msg = String.format("Cannot delete! Category %s is used in %d entries: %s.", 
						category.getName(), 
						entries.size(),
						entries.stream()
							.map(e -> e.getId().toString())
							.collect(Collectors.joining(",", "[", "]")
				));
				throw new TMoneyException(msg);
			}
		}
		
		delete(category);
	}
	
}

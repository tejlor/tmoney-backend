package pl.telech.tmoney.bank.logic.interfaces;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;
import pl.telech.tmoney.commons.model.shared.TableParams;

public interface CategoryLogic extends AbstractLogic<Category> {

	Category create(CategoryDto _category);

	Category update(int id, CategoryDto _category);

	List<Category> loadByAccountCode(String accountCode);

	void delete(int id, Integer newCategoryId);

	Pair<List<Category>, Integer> loadTable(TableParams params);

}

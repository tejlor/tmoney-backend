package pl.telech.tmoney.bank.logic.interfaces;

import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;

public interface CategoryLogic extends AbstractLogic<Category> {

	Category create(Category _category);

	Category update(int id, Category _category);

	void delete(int id);

}

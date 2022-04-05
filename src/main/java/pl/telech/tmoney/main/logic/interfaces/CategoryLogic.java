package pl.telech.tmoney.main.logic.interfaces;

import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;
import pl.telech.tmoney.main.model.entity.Category;

public interface CategoryLogic extends AbstractLogic<Category> {

	Category create(Category _category);

	Category update(int id, Category _category);

	void delete(int id);

}

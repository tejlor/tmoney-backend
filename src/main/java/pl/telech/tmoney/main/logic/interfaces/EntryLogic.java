package pl.telech.tmoney.main.logic.interfaces;

import java.util.List;

import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;
import pl.telech.tmoney.main.model.entity.Entry;

public interface EntryLogic extends AbstractLogic<Entry> {

	List<Entry> loadByCategoryId(int categoryId);

	Entry create(Entry _entry);

	Entry update(int id, Entry _entry);

	void delete(int id);

}

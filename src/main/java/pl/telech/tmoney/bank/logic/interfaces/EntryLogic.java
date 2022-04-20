package pl.telech.tmoney.bank.logic.interfaces;

import java.util.List;

import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;

public interface EntryLogic extends AbstractLogic<Entry> {

	List<Entry> loadByCategoryId(int categoryId);

	Entry create(Entry _entry);

	Entry update(int id, Entry _entry);

	void delete(int id);

	Entry loadLastByAccount(int accountId);

}

package pl.telech.tmoney.bank.logic.interfaces;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;
import pl.telech.tmoney.commons.model.shared.TableParams;

public interface EntryLogic extends AbstractLogic<Entry> {

	List<Entry> loadByCategoryId(int categoryId);

	Entry create(Entry _entry);

	Entry update(int id, Entry _entry);

	void delete(int id);

	Entry loadLastByAccount(int accountId);

	Pair<List<Entry>, Integer> loadAll(String accountCode, TableParams params);

	void updateBalances();

}

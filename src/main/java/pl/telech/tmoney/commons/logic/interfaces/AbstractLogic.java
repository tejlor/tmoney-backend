package pl.telech.tmoney.commons.logic.interfaces;

import java.util.List;

import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

public interface AbstractLogic<T extends AbstractEntity> {
	T loadById(Integer id);
	List<T> loadAll();
	List<T> loadAllById();
}

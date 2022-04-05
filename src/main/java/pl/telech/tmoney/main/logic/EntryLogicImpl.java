package pl.telech.tmoney.main.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.main.dao.EntryDAO;
import pl.telech.tmoney.main.logic.interfaces.EntryLogic;
import pl.telech.tmoney.main.model.entity.Entry;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class EntryLogicImpl extends AbstractLogicImpl<Entry> implements EntryLogic {
	
	EntryDAO dao;
	
	public EntryLogicImpl(EntryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public List<Entry> loadByCategoryId(int categoryId) {
		return dao.findByCategoryId(categoryId);
	}
	
	// ################################### PRIVATE #########################################################################
	
}

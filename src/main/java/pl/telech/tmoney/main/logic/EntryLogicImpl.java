package pl.telech.tmoney.main.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.main.dao.EntryDAO;
import pl.telech.tmoney.main.logic.interfaces.EntryLogic;
import pl.telech.tmoney.main.model.entity.Entry;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class EntryLogicImpl extends AbstractLogicImpl<Entry> implements EntryLogic {
	
	EntryDAO dao;
	
	public EntryLogicImpl(EntryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	// ################################### PRIVATE #########################################################################
	
}

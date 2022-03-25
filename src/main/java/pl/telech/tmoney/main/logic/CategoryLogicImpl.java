package pl.telech.tmoney.main.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.main.dao.CategoryDAO;
import pl.telech.tmoney.main.logic.interfaces.CategoryLogic;
import pl.telech.tmoney.main.model.entity.Category;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class CategoryLogicImpl extends AbstractLogicImpl<Category> implements CategoryLogic {
	
	CategoryDAO dao;
	
	public CategoryLogicImpl(CategoryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	// ################################### PRIVATE #########################################################################
	
}

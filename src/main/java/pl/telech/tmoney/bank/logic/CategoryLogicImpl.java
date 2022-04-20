package pl.telech.tmoney.bank.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.dao.CategoryDAO;
import pl.telech.tmoney.bank.logic.interfaces.CategoryLogic;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class CategoryLogicImpl extends AbstractLogicImpl<Category> implements CategoryLogic {
	
	@Autowired
	EntryLogic entryLogic;
	
	
	public CategoryLogicImpl(CategoryDAO dao) {
		super(dao);
	}
		
	@Override
	public Category create(Category _category) {
		validate(_category);
		
		var category = new Category();		
		category.setName(_category.getName());
		category.setAccount(_category.getAccount());
		category.setReport(_category.getReport());
		category.setDefaultName(_category.getDefaultName());
		category.setDefaultAmount(_category.getDefaultAmount());
		category.setDefaultDescription(_category.getDefaultDescription());
		
		return save(category);
	}
	
	@Override
	public Category update(int id, Category _category) {
		validate(_category);
		
		Category category = loadById(id);
		TUtils.assertEntityExists(category);
				
		category.setName(_category.getName());
		category.setAccount(_category.getAccount());
		category.setReport(_category.getReport());
		category.setDefaultName(_category.getDefaultName());
		category.setDefaultAmount(_category.getDefaultAmount());
		category.setDefaultDescription(_category.getDefaultDescription());
		
		return save(category);
	}
	
	@Override
	public void delete(int id) {
		Category category = loadById(id);
		TUtils.assertEntityExists(category);
		
		List<Entry> entries = entryLogic.loadByCategoryId(id);
		if(!entries.isEmpty()) {
			var msg = String.format("Cannot delete! Category %s is used in %d entries: %s.", 
					category.getName(), 
					entries.size(),
					entries.stream()
						.map(e -> e.getId().toString())
						.collect(Collectors.joining(",", "[", "]")
			));
			throw new TMoneyException(msg);
		}
		
		delete(category);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void validate(Category category) {
		
	}
}

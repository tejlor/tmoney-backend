package pl.telech.tmoney.commons.logic;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@FieldDefaults(level = PROTECTED)
public abstract class AbstractLogicImpl<T extends AbstractEntity> implements AbstractLogic<T> {

	DAO<T> dao;
	
		
	public AbstractLogicImpl(DAO<T> dao){
		this.dao = dao;
	}

	@Override
	public T loadById(Integer id) {
		if(id == null)
			throw new NullPointerException();
		
		return dao.findById(id).orElse(null);
	}

	@Override
	public List<T> loadAll() {
		return dao.findAll();
	}
	
	@Override
	public List<T> loadAllById() {
		return dao.findAllById();
	}
		
	protected T save(T entity){
		return dao.save(entity);
	}
	
	protected T saveAndFlush(T entity){
		return dao.saveAndFlush(entity);
	}
	
	protected void reload(T entity){
		dao.reload(entity);
	}

	protected void delete(T entity) {
		dao.delete(entity);
	}
}

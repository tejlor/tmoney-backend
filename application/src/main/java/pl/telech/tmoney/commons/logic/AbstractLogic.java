package pl.telech.tmoney.commons.logic;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.dao.DAO;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@RequiredArgsConstructor
@FieldDefaults(level = PROTECTED)
public abstract class AbstractLogic<T extends AbstractEntity> {

	DAO<T> dao;
	

	public T loadById(@NonNull Integer id) {
		return dao.findById(id).orElseThrow();
	}

	public List<T> loadAll() {
		return dao.findAll();
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

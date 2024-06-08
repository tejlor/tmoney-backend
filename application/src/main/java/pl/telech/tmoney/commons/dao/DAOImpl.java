package pl.telech.tmoney.commons.dao;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Base implementation of all repository classes. Adds some methods helpful in using specifications and entity graphs. 
 */
public class DAOImpl<T extends AbstractEntity> extends SimpleJpaRepository<T, Integer> implements DAO<T> {

	final EntityManager entityManager;
	

	public DAOImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager){
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}	
	
	@Override
	public TQuery<T> where() {
		return new TQuery<T>(entityManager, this::getQuery, this::count);
	}
	
	@Override
	public TQuery<T> where(Specification<T> specification) {
		return where().and(specification);
	}
	
	@Override
	public void detach(T entity) {
        entityManager.detach(entity);
    }
	
	@Override
	public void reload(T entity) {
        entityManager.refresh(entity);
    }
	
}

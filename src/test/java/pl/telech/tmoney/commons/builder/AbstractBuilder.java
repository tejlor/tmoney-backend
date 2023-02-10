package pl.telech.tmoney.commons.builder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import lombok.Setter;
import lombok.experimental.Accessors;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@Setter
@Accessors(chain = true, fluent = true)
public abstract class AbstractBuilder<T extends AbstractEntity> {

	Integer id = null;
	
	public T save(EntityManager em) {
		var result = build();
		persistDependencies(em);
		em.persist(result);
		return result;
	}
	
	public T saveAndReload(EntityManager em) {
		var result = build();
		persistDependencies(em);
		em.persist(result);
		em.flush();
		em.refresh(result);
		return result;
	}
	
	public abstract T build();
	
	protected void persistDependencies(EntityManager em) {}
	
	protected void fill(T entity) {
		entity.setId(id);
	}
}

package pl.telech.tmoney.commons.builder;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public abstract class AbstractBuilder<T extends AbstractEntity> {

	Integer id = null;
	
	public abstract T build();
	public abstract void persistDependences(EntityManager em);
	
	public T save(EntityManager em) {
		var result = build();
		persistDependences(em);
		em.persist(result);
		return result;
	}
	
	public T saveAndReload(EntityManager em) {
		var result = build();
		persistDependences(em);
		em.persist(result);
		em.flush();
		em.refresh(result);
		return result;
	}
	
	protected void fill(T entity) {
		entity.setId(id);
	}
}

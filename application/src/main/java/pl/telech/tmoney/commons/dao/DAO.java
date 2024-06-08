package pl.telech.tmoney.commons.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Base interface for all repository interfaces.
 */
@NoRepositoryBean
public interface DAO<T extends AbstractEntity> extends JpaRepository<T, Integer> {

	TQuery<T> where();
	
	TQuery<T> where(Specification<T> secification);

	void detach(T entity);

	void reload(T entity);
}

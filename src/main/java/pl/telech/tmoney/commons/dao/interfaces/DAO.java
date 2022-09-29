package pl.telech.tmoney.commons.dao.interfaces;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Base interface for all Repository interfaces.
 */
@NoRepositoryBean
public interface DAO<T extends AbstractEntity> extends JpaRepository<T, Integer> {

	@SuppressWarnings("unchecked")
	T findOne(Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	T findOne(String entityGraphName, Specification<T> ...spec);

	List<T> findAllById();
	
	@SuppressWarnings("unchecked")
	List<T> findAll(Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	List<T> findAll(String entityGraphName, Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	List<T> findAll(Sort sort, Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	List<T> findAll(String entityGraphName, Sort sort, Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	Pair<List<T>, Integer> findAllWithCount(Pageable page, Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	Pair<List<T>, Integer> findAllWithCount(String entityGraphName, Pageable page, Specification<T> ...spec);

	void detach(T entity);

	void reload(T entity);
}

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
 * Base interface for all repository interfaces.
 */
@NoRepositoryBean
public interface DAO<T extends AbstractEntity> extends JpaRepository<T, Integer> {

	T findOne(Specification<T> ...spec);
	
	T findOne(Pageable page, Specification<T> ...spec);
	
	T findOne(String entityGraphName, Specification<T> ...spec);
	
	T findOne(String entityGraphName, Pageable page, Specification<T> ...spec);

	List<T> findAllById();
	
	List<T> findAll(Specification<T> ...spec);
	
	List<T> findAll(String entityGraphName, Specification<T> ...spec);
	
	List<T> findAll(Sort sort, Specification<T> ...spec);
	
	List<T> findAll(Pageable page, Specification<T> ...spec);
	
	List<T> findAll(String entityGraphName, Sort sort, Specification<T> ...spec);
	
	List<T> findAll(String entityGraphName, Pageable page, Specification<T> ...spec);
	
	Pair<List<T>, Integer> findAllWithCount(Pageable page, Specification<T> ...spec);
	
	Pair<List<T>, Integer> findAllWithCount(String entityGraphName, Pageable page, Specification<T> ...spec);

	void detach(T entity);

	void reload(T entity);
}

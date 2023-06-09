package pl.telech.tmoney.commons.dao;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.shared.TableParams;

/*
 * Base interface for all repository interfaces.
 */
@NoRepositoryBean
public interface DAO<E extends AbstractEntity> extends JpaRepository<E, Integer>, JpaSpecificationExecutor<E> {

	E findOne(Specification<E> ...spec);
	
	E findOne(String entityGraphName, Specification<E> ...spec);

	List<E> findManyById();
	
	List<E> findMany(Specification<E> ...spec);
	
	List<E> findMany(String entityGraphName, Specification<E> ...spec);
	
	List<E> findMany(Sort sort, Specification<E> ...spec);
	
	List<E> findMany(Pageable page, Specification<E> ...spec);
	
	List<E> findMany(String entityGraphName, Sort sort, Specification<E> ...spec);
	
	List<E> findMany(String entityGraphName, Pageable page, Specification<E> ...spec);
	
	Pair<List<E>, Integer> findTable(TableParams tableParams, Function<String, Specification<E>> isLike);
	
	Pair<List<E>, Integer> findManyWithCount(Pageable page, Specification<E> ...spec);
	
	Pair<List<E>, Integer> findManyWithCount(String entityGraphName, Pageable page, Specification<E> ...spec);

	void detach(E entity);

	void reload(E entity);
	
	Specification<E> isLike(String filter);
	
}

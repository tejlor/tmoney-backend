package pl.telech.tmoney.commons.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.shared.TableParams;

/*
 * Base implementation of all repository classes. Adds some methods helpful in using specifications and entity graphs. 
 */
public class DAOImpl<E extends AbstractEntity> extends SimpleJpaRepository<E, Integer> implements DAO<E> {

	final EntityManager entityManager;
	

	public DAOImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager){
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
		
	@Override
	public E findOne(Specification<E> ...spec) {
        return findOne(null, spec);
    }
	
	@Override
	public E findOne(String entityGraphName, Specification<E> ...spec) {
        TypedQuery<E> query = getQuery(conjunction(spec), (Sort) null);
        if (entityGraphName != null) {
        	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
        }          
        return query.getSingleResult();
    }
	
	@Override
	public List<E> findManyById() {
		return findMany(null, Sort.by(AbstractEntity.Fields.id), (Specification<E>) null);
	}
	
	@Override
	public List<E> findMany(Specification<E> ...spec) {
	    return findMany(null, Sort.unsorted(), spec);
	}
	
	@Override
	public List<E> findMany(Sort sort, Specification<E> ...spec) {
		 return findMany(null, sort, spec);
	}
	
	@Override
	public List<E> findMany(Pageable page, Specification<E> ...spec) {
		 return findMany(null, page, spec);
	}
	
	@Override
	public List<E> findMany(String entityGraphName, Specification<E> ...spec) {
		 return findMany(entityGraphName, Sort.unsorted(), spec);
	}
	
	@Override
	public List<E> findMany(String entityGraphName, Sort sort, Specification<E> ...spec) {
	    TypedQuery<E> query = getQuery(conjunction(spec), sort);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    return query.getResultList();
	}
	
	@Override
	public List<E> findMany(String entityGraphName, Pageable page, Specification<E> ...spec) {
	    TypedQuery<E> query = getQuery(conjunction(spec), page);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    if(page.isPaged()) {
	    	query.setFirstResult((int)page.getOffset());
	    	query.setMaxResults(page.getPageSize());
	    }
	    
	    return query.getResultList();
	}
	
	@Override
	public Pair<List<E>, Integer> findTable(TableParams tableParams, Function<String, Specification<E>> isLike){
		return findManyWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike.apply(tableParams.getFilter()) : null
				);
	}
	
	@Override
	public Pair<List<E>, Integer> findManyWithCount(Pageable page, Specification<E> ...spec) {
		 return findManyWithCount(null, page, spec);
	}
	
	@Override
	public Pair<List<E>, Integer> findManyWithCount(String entityGraphName, Pageable page, Specification<E> ...spec) {
	    Specification<E> specSum = conjunction(spec);
		TypedQuery<E> query = getQuery(specSum, page);
	    if (entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    query.setFirstResult((int)page.getOffset());
	    query.setMaxResults(page.getPageSize());
	    
	    return Pair.of(query.getResultList(), (int) count(specSum));
	}
	
	@SafeVarargs
	private Specification<E> conjunction(Specification<E> ...specs){
		return Arrays.stream(specs)
			.filter(Objects::nonNull)
			.reduce(Specification::and)
			.orElse(null);
	}
	
	@Override
	public void detach(E entity) {
        entityManager.detach(entity);
    }
	
	@Override
	public void reload(E entity) {
        entityManager.refresh(entity);
    }
	
	@Override
	public Specification<E> isLike(String filter) {
        return (account, cq, cb) -> {
        	return null;
        };
	}
	
}

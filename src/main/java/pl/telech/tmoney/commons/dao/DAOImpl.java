package pl.telech.tmoney.commons.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
	public T findOne(Specification<T> ...spec) {
        return findOne(null, spec);
    }
	
	@Override
	public T findOne(String entityGraphName, Specification<T> ...spec) {
        TypedQuery<T> query = getQuery(conjunction(spec), (Sort) null);
        if (entityGraphName != null) {
        	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
        }          
        return query.getSingleResult();
    }
	
	@Override
	public List<T> findManyById() {
		return findMany(null, Sort.by(AbstractEntity.Fields.id), (Specification<T>) null);
	}
	
	@Override
	public List<T> findMany(Specification<T> ...spec) {
	    return findMany(null, Sort.unsorted(), spec);
	}
	
	@Override
	public List<T> findMany(Sort sort, Specification<T> ...spec) {
		 return findMany(null, sort, spec);
	}
	
	@Override
	public List<T> findMany(Pageable page, Specification<T> ...spec) {
		 return findMany(null, page, spec);
	}
	
	@Override
	public List<T> findMany(String entityGraphName, Specification<T> ...spec) {
		 return findMany(entityGraphName, Sort.unsorted(), spec);
	}
	
	@Override
	public List<T> findMany(String entityGraphName, Sort sort, Specification<T> ...spec) {
	    TypedQuery<T> query = getQuery(conjunction(spec), sort);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    return query.getResultList();
	}
	
	@Override
	public List<T> findMany(String entityGraphName, Pageable page, Specification<T> ...spec) {
	    TypedQuery<T> query = getQuery(conjunction(spec), page);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    if(page.isPaged()) {
	    	query.setFirstResult((int)page.getOffset());
	    	query.setMaxResults(page.getPageSize());
	    }
	    
	    return query.getResultList();
	}
	
	@Override
	public Pair<List<T>, Integer> findManyWithCount(Pageable page, Specification<T> ...spec) {
		 return findManyWithCount(null, page, spec);
	}
	
	@Override
	public Pair<List<T>, Integer> findManyWithCount(String entityGraphName, Pageable page, Specification<T> ...spec) {
	    Specification<T> specSum = conjunction(spec);
		TypedQuery<T> query = getQuery(specSum, page);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    query.setFirstResult((int)page.getOffset());
	    query.setMaxResults(page.getPageSize());
	    
	    return Pair.of(query.getResultList(), (int) count(specSum));
	}
	
	@SafeVarargs
	private Specification<T> conjunction(Specification<T> ...specs){
		return Arrays.stream(specs)
			.filter(Objects::nonNull)
			.reduce(Specification::and)
			.orElse(null);
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

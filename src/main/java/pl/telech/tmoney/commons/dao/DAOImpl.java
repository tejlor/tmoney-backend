package pl.telech.tmoney.commons.dao;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Base implementation of all repository classes. Adds some methods helpful in using specifications and entity graphs. 
 */
public class DAOImpl<T extends AbstractEntity> extends SimpleJpaRepository<T, Integer> implements DAO<T> {

	EntityManager entityManager;

	public DAOImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager){
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
	
	@Override
	public T findOne(Specification<T> ...spec) {
        return findOne(null, null, spec);
    }
	
	@Override
	public T findOne(String entityGraphName, Specification<T> ...spec) {
        return findOne(entityGraphName, null, spec);
    }
	
	@Override
	public T findOne(Pageable page, Specification<T> ...spec) {
        return findOne(null, page, spec);
    }
	
	@Override
	public T findOne(String entityGraphName, Pageable page, Specification<T> ...spec) {
        TypedQuery<T> query = getQuery(conjunction(spec), page);
        if(entityGraphName != null)
        	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
        
        if(page.isPaged()) {
	    	query.setFirstResult((int)page.getOffset());
	    	query.setMaxResults(page.getPageSize());
	    }
        
        return query.getSingleResult();
    }
	
	@Override
	public List<T> findAllById() {
		return getQuery(null, Sort.by("id")).getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Specification<T> ...spec) {
	    return findAll(null, (Sort) null, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Sort sort, Specification<T> ...spec) {
		 return findAll(null, sort, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Pageable page, Specification<T> ...spec) {
		 return findAll(null, page, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Specification<T> ...spec) {
		 return findAll(entityGraphName, Sort.unsorted(), spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Sort sort, Specification<T> ...spec) {
	    TypedQuery<T> query = getQuery(conjunction(spec), sort);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Pageable page, Specification<T> ...spec) {
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
	@SuppressWarnings("unchecked")
	public Pair<List<T>, Integer> findAllWithCount(Pageable page, Specification<T> ...spec) {
		 return findAllWithCount(null, page, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Pair<List<T>, Integer> findAllWithCount(String entityGraphName, Pageable page, Specification<T> ...spec) {
	    Specification<T> specSum = conjunction(spec);
		TypedQuery<T> query = getQuery(specSum, page);
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    query.setFirstResult((int)page.getOffset());
	    query.setMaxResults(page.getPageSize());
	    
	    return Pair.of(query.getResultList(), (int) count(specSum));
	}
	
	@SafeVarargs
	private final Specification<T> conjunction(Specification<T> ...specs){
		return Arrays.stream(specs)
			.filter(s -> s != null)
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

package pl.telech.tmoney.commons.dao;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@Getter
@RequiredArgsConstructor
public class TQuery<E extends AbstractEntity> {

	final EntityManager em;
	final BiFunction<Specification<E>, Sort, TypedQuery<E>> getQuery;
	final Function<Specification<E>, Long> count;
	
	Specification<E> specification;
	Sort sort = Sort.unsorted();
	Pageable page = Pageable.unpaged();
	String entityGraph;

	public TQuery<E> and(Specification<E> otherSpecification) {
		if (specification == null) {
			specification = otherSpecification;
		}
		else if (otherSpecification != null) {
			this.specification = this.specification.and(otherSpecification);
		}
		return this;
	}

	public TQuery<E> orderBy(Sort sort) {
		this.sort = sort;
		return this;
	}

	public TQuery<E> withPage(Pageable page) {
		this.page = page;
		return this;
	}

	public TQuery<E> withGraph(String entityGraph) {
		this.entityGraph = entityGraph;
		return this;
	}

	public E findOne() {
		TypedQuery<E> query = getQuery.apply(specification, sort);
		if (entityGraph != null) {
			query.setHint(EntityGraphType.FETCH.getKey(), em.getEntityGraph(entityGraph));
		}

		return query.getSingleResult();
	}

	public List<E> findMany() {
		TypedQuery<E> query = getQuery.apply(specification, sort);
		if (entityGraph != null) {
			query.setHint(EntityGraphType.FETCH.getKey(), em.getEntityGraph(entityGraph));
		}

		if (page.isPaged()) {
			query.setFirstResult((int) page.getOffset());
			query.setMaxResults(page.getPageSize());
		}

		return query.getResultList();
	}

	public Pair<List<E>, Integer> findManyWithCount() {
		TypedQuery<E> query = getQuery.apply(specification, sort);
		if (entityGraph != null) {
			query.setHint(EntityGraphType.FETCH.getKey(), em.getEntityGraph(entityGraph));
		}

		if (page.isPaged()) {
			query.setFirstResult((int) page.getOffset());
			query.setMaxResults(page.getPageSize());
		}

		return Pair.of(query.getResultList(), count.apply(specification).intValue());
	}

}

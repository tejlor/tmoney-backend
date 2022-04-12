package pl.telech.tmoney.main.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.main.model.entity.Entry;
import pl.telech.tmoney.main.model.entity.Entry.Fields;


public interface EntryDAO extends DAO<Entry>, JpaSpecificationExecutor<Entry> {
	
	List<Entry> findByCategoryId(int id);
	
	@SuppressWarnings("unchecked")
	default Entry findLastBeforeDate(LocalDate date) {
		return findAll(getPage(),
				isBefore(date))
			.get(0);
	}
	
	@SuppressWarnings("unchecked")
	default Entry findLastByAccountBeforeDate(int accountId, LocalDate date) {
		return findAll(getPage(),
				hasAccount(accountId),
				isBefore(date))
			.get(0);
	}
		
	// ######################### Specifications #########################################################################################################
		
	private Specification<Entry> isLike(String filter) {
        return (entry, cq, cb) -> {
        	return cb.or(
        		cb.like(cb.lower(entry.get(Fields.name)), "%" + filter + "%"), 
        		cb.like(cb.lower(entry.get(Fields.description)), "%" + filter + "%")
        	);
        };
	}
	
	private Specification<Entry> hasAccount(int accountId) {
        return (entry, cq, cb) -> {
        	return cb.equal(entry.get(Fields.accountId), accountId);
        };
	}
	
	private Specification<Entry> isBefore(LocalDate date) {
        return (entry, cq, cb) -> {
        	return cb.lessThanOrEqualTo(entry.get(Fields.date), date);
        };
	}
	
	private Pageable getPage() {
		return PageRequest.of(0, 1, Sort.by(Direction.DESC, Fields.date, AbstractEntity.Fields.id));
	}
}

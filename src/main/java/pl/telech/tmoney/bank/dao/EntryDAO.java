package pl.telech.tmoney.bank.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.Entry.Fields;
import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface EntryDAO extends DAO<Entry>, JpaSpecificationExecutor<Entry> {
	
	List<Entry> findByCategoryId(int id);
	
	default Pair<List<Entry>, Integer> findTableByAccountId(Integer accountId, TableParams tableParams){
		return findAllWithCount(
				Entry.GRAPH_WITH_CATEGORY,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null,
				accountId != null ? belongsToAccount(accountId): null
				);
	}
	
	default List<Entry> findByAccountId(Integer accountId){
		return findAll(
				Entry.GRAPH_WITH_CATEGORY,
				getSort(),
				accountId != null ? belongsToAccount(accountId): null
				);
	}
	
	default Entry findLastBeforeDate(LocalDate date) {
		return findAll(getSortDesc(),
				isBefore(date))
			.get(0);
	}
	
	default Entry findLastByAccountBeforeDate(int accountId, LocalDate date) {
		return findAll(getSortDesc(),
				belongsToAccount(accountId),
				isBefore(date))
			.get(0);
	}
	
	@Modifying
	@Query(value = "CALL bank.updateBalances()", nativeQuery = true)
	void updateBalances();
		
	// ######################### Specifications #########################################################################################################
		
	private Specification<Entry> isLike(String filter) {
        return (entry, cq, cb) -> {
        	return cb.or(
        		cb.like(cb.lower(entry.get(Fields.name)), "%" + filter + "%"), 
        		cb.like(cb.lower(entry.get(Fields.description)), "%" + filter + "%")
        	);
        };
	}
	
	private Specification<Entry> belongsToAccount(int accountId) {
        return (entry, cq, cb) -> {
        	return cb.equal(entry.get(Fields.accountId), accountId);
        };
	}
	
	private Specification<Entry> isBefore(LocalDate date) {
        return (entry, cq, cb) -> {
        	return cb.lessThanOrEqualTo(entry.get(Fields.date), date);
        };
	}
	
	private Sort getSort() {
		return Sort.by(Fields.date, AbstractEntity.Fields.id);
	}
	
	private Sort getSortDesc() {
		return Sort.by(Direction.DESC, Fields.date, AbstractEntity.Fields.id);
	}
}

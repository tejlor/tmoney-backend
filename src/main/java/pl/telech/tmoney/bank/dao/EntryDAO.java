package pl.telech.tmoney.bank.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.criteria.Join;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.telech.tmoney.bank.dao.data.CategoryAmount;
import pl.telech.tmoney.bank.dao.data.EntryAmount;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.entity.Entry.Fields;
import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface EntryDAO extends DAO<Entry>, JpaSpecificationExecutor<Entry> {
	
	List<Entry> findByCategoryId(int id);
	
	default Pair<List<Entry>, Integer> findTableByAccountId(Integer accountId, TableParams tableParams){
		return findManyWithCount(
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null,
				accountId != null ? belongsToAccount(accountId): null
				);
	}
		
	default List<Entry> findByAccountId(Integer accountId){
		return findMany(
				SortAsc,
				accountId != null ? belongsToAccount(accountId): includesInSummary()
				);
	}
	
	default Entry findLastBeforeDate(LocalDate date) {
		List<Entry> result = findMany(PageRequest.of(0, 1, SortDesc),
				isBefore(date)
		);
		
		return CollectionUtils.isNotEmpty(result) && result.size() == 1 
				? result.get(0)
				: null;
	}
	
	default Entry findLastByAccountBeforeDate(int accountId, LocalDate date) {
		List<Entry> result = findMany(PageRequest.of(0, 1, SortDesc),
				belongsToAccount(accountId),
				isBefore(date)
		);
		return CollectionUtils.isNotEmpty(result) && result.size() == 1 
				? result.get(0)
				: null;
	}
		
	@Query("SELECT SUM(e.amount) "
		 + "FROM Entry e "
		 + "WHERE e.accountId = :accountId "
		 	+ "AND e.date BETWEEN :dateFrom AND :dateTo "
		 	+ "AND e.amount > 0")
	BigDecimal findAccountIncome(@Param("accountId") int accountId, @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
	
	@Query("SELECT -SUM(e.amount) "
		 + "FROM Entry e "
		 + "WHERE e.accountId = :accountId "
		 	+ "AND e.date BETWEEN :dateFrom AND :dateTo "
		 	+ "AND e.amount < 0")
	BigDecimal findAccountOutcome(@Param("accountId") int accountId, @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
	
	@Query("SELECT NEW " + CategoryAmount.TYPE + "(e.category.name, SUM(e.amount)) "
		 + "FROM Entry e "
		 + "WHERE e.date BETWEEN :dateFrom AND :dateTo "
		 	+ "AND e.amount > 0 "
		 	+ "AND e.category.report = TRUE "
		 	+ "AND e.account.includeInSummary = TRUE "
		 + "GROUP BY e.category.name ")
	List<CategoryAmount> findSummaryIncomeByCategory(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
	
	@Query("SELECT NEW " + CategoryAmount.TYPE + "(e.category.name, -SUM(e.amount)) "
		 + "FROM Entry e "
		 + "WHERE e.date BETWEEN :dateFrom AND :dateTo "
		 	+ "AND e.amount < 0 "
		 	+ "AND e.category.report = TRUE "
		 	+ "AND e.account.includeInSummary = TRUE "
		 + "GROUP BY e.category.name ")
	List<CategoryAmount> findSummaryOutcomeByCategory(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
	
	@Query("SELECT NEW " + EntryAmount.TYPE + "(e.date, e.amount) "
		 + "FROM Entry e "
		 + "WHERE e.date BETWEEN :dateFrom AND :dateTo "
		 	+ "AND e.category.report = TRUE "
		 	+ "AND e.account.includeInSummary = TRUE")
	List<EntryAmount> findEntriesForReport(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
	
	/*
	 * @Procedure annotation doesn't work. Exceptions says: "bank.updateBalances is procedure. Use call." 
	 */
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
	
	private Specification<Entry> includesInSummary() {
        return (entry, cq, cb) -> {
        	Join<Account, Entry> account = entry.join(Fields.account);
        	return cb.equal(account.get(Account.Fields.includeInSummary), true);
        };
	}
	
	private Specification<Entry> isBefore(LocalDate date) {
        return (entry, cq, cb) -> {
        	return cb.lessThan(entry.get(Fields.date), date);
        };
	}
	
	static final Sort SortAsc = Sort.by(Fields.date, AbstractEntity.Fields.id);	
	static final Sort SortDesc = Sort.by(Direction.DESC, Fields.date, AbstractEntity.Fields.id);
}

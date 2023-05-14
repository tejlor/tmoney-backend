package pl.telech.tmoney.bank.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.telech.tmoney.bank.dao.data.CategoryAmount;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Account.Fields;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.dao.DAO;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface AccountDAO extends DAO<Account>, JpaSpecificationExecutor<Account> {
		
	Account findByCode(String code);
	
	default List<Account> findWithEntries(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo) {
		return findMany(BY_ORDER_NO,
				hasEntries(dateFrom, dateTo));
	}
	
	default List<Account> findAll(boolean onlyActive) {
		return findMany(BY_ORDER_NO, 
				onlyActive ? isActive() : null);
	}
	
	default Pair<List<Account>, Integer> findTable(TableParams tableParams) {
		return findManyWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	// ######################### Specifications ################################################################################################
		
	private Specification<Account> isActive() {
        return (account, cq, cb) -> {
        	return cb.equal(account.get(Fields.active), true);
        };
	}
	
	private Specification<Account> hasEntries(LocalDate dateFrom, LocalDate dateTo) {
        return (account, cq, cb) -> {
        	Subquery<Entry> subquery = cq.subquery(Entry.class);
        	Root<Entry> entry = subquery.from(Entry.class);
        	subquery.select(entry)
        		.where(cb.equal(entry.get(Entry.Fields.account), account),
        			   cb.between(entry.get(Entry.Fields.date), dateFrom, dateTo));
        	return cb.exists(subquery);
        };
	}
	
	private Specification<Account> isLike(String filter) {
        return (account, cq, cb) -> {
        	return cb.or(
            		cb.like(cb.lower(account.get(Fields.name)), "%" + filter + "%"), 
            		cb.like(cb.lower(account.get(Fields.code)), "%" + filter + "%")
            	);
        };
	}
	
	// ######################### Pages #########################################################################################################
	
	final Sort BY_ORDER_NO = Sort.by(Fields.orderNo);
	
}

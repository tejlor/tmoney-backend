package pl.telech.tmoney.bank.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Account.Fields;
import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface AccountDAO extends DAO<Account>, JpaSpecificationExecutor<Account> {
		
	Account findByCode(String code);
	
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

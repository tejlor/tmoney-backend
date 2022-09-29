package pl.telech.tmoney.bank.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Account.Fields;
import pl.telech.tmoney.commons.dao.interfaces.DAO;


public interface AccountDAO extends DAO<Account>, JpaSpecificationExecutor<Account> {
		
	Account findByCode(String code);
	
	default List<Account> findActive() {
		return findAll(BY_ORDER_NO, isActive());
	}
	
	// ######################### Specifications ################################################################################################
		
	private Specification<Account> isActive() {
        return (account, cq, cb) -> {
        	return cb.equal(account.get(Fields.active), true);
        };
	}
	
	// ######################### Pages #########################################################################################################
	
	final Sort BY_ORDER_NO = Sort.by(Fields.orderNo);
	
}

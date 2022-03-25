package pl.telech.tmoney.main.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.main.model.entity.Account;


public interface AccountDAO extends DAO<Account>, JpaSpecificationExecutor<Account> {
	
	// ######################### Specifications #########################################################################################################
		
}

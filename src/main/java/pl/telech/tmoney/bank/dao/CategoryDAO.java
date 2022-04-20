package pl.telech.tmoney.bank.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.dao.interfaces.DAO;


public interface CategoryDAO extends DAO<Category>, JpaSpecificationExecutor<Category> {
	
	
	// ######################### Specifications #########################################################################################################
		

}

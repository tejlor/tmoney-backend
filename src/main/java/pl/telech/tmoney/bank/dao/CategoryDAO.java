package pl.telech.tmoney.bank.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Category.Fields;
import pl.telech.tmoney.commons.dao.interfaces.DAO;


public interface CategoryDAO extends DAO<Category>, JpaSpecificationExecutor<Category> {
	
	
	// ######################### Specifications #########################################################################################################
		
	
	private Specification<Category> isLike(String filter) {
        return (entry, cq, cb) -> {
        	return cb.like(cb.lower(entry.get(Fields.name)), "%" + filter + "%");
        };
	}
	
}

package pl.telech.tmoney.adm.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.adm.model.entity.User.Fields;
import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface UserDAO extends DAO<User>, JpaSpecificationExecutor<User> {

	User findByEmail(String email);
	
	@SuppressWarnings("unchecked")
	default Pair<List<User>, Integer> findAll(TableParams tableParams){
		return findManyWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	// ######################### Specifications #########################################################################################################
		
	private Specification<User> isLike(String filter){
        return (element, cq, cb) -> {
        	return cb.or(
        		cb.like(cb.lower(element.get(Fields.firstName)), "%" + filter + "%"), 
        		cb.like(cb.lower(element.get(Fields.lastName)), "%" + filter + "%"),
        		cb.like(cb.lower(element.get(Fields.email)), "%" + filter + "%")
        	);
        };
	}
}

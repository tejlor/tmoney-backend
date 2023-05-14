package pl.telech.tmoney.bank.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Category.Fields;
import pl.telech.tmoney.commons.dao.DAO;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface CategoryDAO extends DAO<Category>, JpaSpecificationExecutor<Category> {
	
	default Pair<List<Category>, Integer> findTable(TableParams tableParams){
		return findManyWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	@Modifying
	@Query(value = "UPDATE Entry SET category.id = :newCategoryId WHERE category.id = :categoryId")
	void changeCategory(@Param("categoryId") int categoryId, @Param("newCategoryId") int newCategoryId);
	
	// ######################### Specifications #########################################################################################################
		
	
	private Specification<Category> isLike(String filter) {
        return (category, cq, cb) -> {
        	return cb.or(
            		cb.like(cb.lower(category.get(Fields.name)), "%" + filter + "%"), 
            		cb.like(cb.lower(category.get(Fields.defaultName)), "%" + filter + "%"),
            		cb.like(cb.lower(category.get(Fields.defaultDescription)), "%" + filter + "%")
            	);
        };
	}
	
}

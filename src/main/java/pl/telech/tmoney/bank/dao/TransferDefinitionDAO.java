package pl.telech.tmoney.bank.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.bank.model.entity.TransferDefinition.Fields;
import pl.telech.tmoney.commons.dao.DAO;
import pl.telech.tmoney.commons.model.shared.TableParams;


public interface TransferDefinitionDAO extends DAO<TransferDefinition>, JpaSpecificationExecutor<TransferDefinition> {
		
	
	default Pair<List<TransferDefinition>, Integer> findTable(TableParams tableParams) {
		return findManyWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	// ######################### Specifications ################################################################################################
		
	default Specification<TransferDefinition> isLike(String filter) {
        return (def, cq, cb) -> {
        	return cb.or(
            		cb.like(cb.lower(def.get(Fields.name)), "%" + filter + "%"), 
            		cb.like(cb.lower(def.get(Fields.description)), "%" + filter + "%")
            	);
        };
	}

	
	// ######################### Pages #########################################################################################################
	
	
}

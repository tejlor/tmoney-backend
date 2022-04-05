package pl.telech.tmoney.main.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.commons.dao.interfaces.DAO;
import pl.telech.tmoney.main.model.entity.Entry;


public interface EntryDAO extends DAO<Entry>, JpaSpecificationExecutor<Entry> {
	
	List<Entry> findByCategoryId(int id);
	
	// ######################### Specifications #########################################################################################################
		
}

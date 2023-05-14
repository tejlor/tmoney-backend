package pl.telech.tmoney.bank.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Category of entry.
 */
@Data
@Entity
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(schema = "bank")
public class Category extends AbstractEntity {
		
	@Column(length = 100, nullable = false)
	String name;				// category name
	
	@Column(nullable = false)
	Integer account;			// m2m accounts
	
	@Column(nullable = false)
	Boolean report;				// if entries should be inculed in reports
	
	@Column(length = 100)
	String defaultName;			// default entry name
	
	@Column
	BigDecimal defaultAmount;	// default entry amount 
	
	@Column(length = 255)
	String defaultDescription;	// default entry description
				
}

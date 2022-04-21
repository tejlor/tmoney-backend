package pl.telech.tmoney.bank.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Category.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Table(name = "category", schema = "bank")
public class Category extends AbstractEntity {
		
	@Column(length = 100, nullable = false)
	String name;				// account name
	
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
				

	public Category(Integer id) {
		super(id);
	}

}

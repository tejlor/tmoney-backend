package pl.telech.tmoney.bank.model.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

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
@NamedEntityGraph(
	name = "Category.all",
	attributeNodes = {
	    @NamedAttributeNode("accounts")
	})
public class Category extends AbstractEntity {
		
	@Column(length = 100, nullable = false)
	String name;				// category name
	
	@ManyToMany
	@JoinTable(
		schema = "bank",
		name = "category_to_account", 
		joinColumns = @JoinColumn(name = "category_id"), 
		inverseJoinColumns = @JoinColumn(name = "account_id"))
	List<Account> accounts;		// m2m accounts
	
	@Column(nullable = false)
	Boolean report;				// if entries should be inculed in reports
	
	@Column(length = 100)
	String defaultName;			// default entry name
	
	@Column
	BigDecimal defaultAmount;	// default entry amount 
	
	@Column(length = 255)
	String defaultDescription;	// default entry description
				
}

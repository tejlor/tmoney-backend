package pl.telech.tmoney.bank.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Entry (row in table).
 */
@Data
@Entity
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(schema = "bank")
@NamedEntityGraph(name = "Entry.category", attributeNodes = {@NamedAttributeNode("category")})
public class Entry extends AbstractEntity {
		
	@Setter(AccessLevel.PRIVATE)
    @Column(insertable = false, updatable = false)
    Integer accountId;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "accountId")
	Account account;				// account
	
	@Column(nullable = false)
	LocalDate date;					// entry date
		
	@ManyToOne(optional = false)
    @JoinColumn(name = "categoryId")
	Category category;				// category
	
	@Column(length = 100, nullable = false)
	String name;					// entry name
	
	@Column(nullable = false)
	BigDecimal amount;				// entry amount 
	
	@Column(length = 255)
	String description;				// entry description
	
	@Column
	BigDecimal balance;				// balance in current account
	
	@Column
	BigDecimal balanceOverall;		// balance in whole portfolio
				
	
	public void setAccount(Account account) {
		this.account = account;
		this.accountId = account != null ? account.getId() : null;
	}
	
}

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
@Table(name = "entry", schema = "bank")
public class Entry extends AbstractEntity {
		
	@Setter(AccessLevel.PRIVATE)
    @Column(insertable = false, updatable = false)
    Integer accountId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountId")
	Account account;				// account
	
	@Column(nullable = false)
	LocalDate date;					// entry date
	
	@Setter(AccessLevel.PRIVATE)
    @Column(insertable = false, updatable = false)
    Integer categoryId;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoryId")
	Category category;				// category
	
	@Column(length = 100, nullable = false)
	String name;					// entry name
	
	@Column(nullable = false)
	BigDecimal amount;				// entry amount 
	
	@Column(length = 255)
	String description;				// entry description
	
	@Column(nullable = false)
	BigDecimal balance;				// balance in current account
	
	@Column(nullable = false)
	BigDecimal balanceOverall;		// balance in whole portfolio
				
	
	public void setAccount(Account account) {
		this.account = account;
		this.accountId = account != null ? account.getId() : null;
	}
	
	public void setCategory(Category category) {
		this.category = category;
		this.categoryId = category != null ? category.getId() : null;
	}

}

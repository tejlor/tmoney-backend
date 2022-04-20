package pl.telech.tmoney.bank.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Entry.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Table(name = "entry", schema = "public")
public class Entry extends AbstractEntity {
	
	@Setter(AccessLevel.PRIVATE)
    @Column(insertable = false, updatable = false)
    Integer accountId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountId")
	Account account;			// account
	
	@Column(nullable = false)
	LocalDate date;				// entry date
	
	@Setter(AccessLevel.PRIVATE)
    @Column(insertable = false, updatable = false)
    Integer categoryId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId")
	Category category;			// category
	
	@Column(length = 100, nullable = false)
	String name;				// entry name
	
	@Column(nullable = false)
	BigDecimal amount;			// entry amount 
	
	@Column(length = 255)
	String description;			// entry description
	
	@Column(nullable = false)
	BigDecimal balance;			// balance in current account
	
	@Column(nullable = false)
	BigDecimal balanceOverall;	// balance in whole portfolio
				

	public Entry(Integer id) {
		super(id);
	}
	
	public void setAccount(Account account) {
		this.account = account;
		this.accountId = account != null ? account.getId() : null;
	}
	
	public void setCategory(Category category) {
		this.category = category;
		this.categoryId = category != null ? category.getId() : null;
	}

}

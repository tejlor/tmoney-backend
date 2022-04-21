package pl.telech.tmoney.bank.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Account.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Table(name = "account", schema = "bank")
public class Account extends AbstractEntity {
		
	@Column(length = 10, nullable = false)
	String code;				// technical code
	
	@Column(length = 100, nullable = false)
	String name;				// account name
	
	@Column(nullable = false)
	Boolean active;				// if account is active and should be visible at front
	
	@Column(length = 100) 
	String color;				// color in rgb format
	
	@Column(length = 3)
	String orderNo;				// account order at front in format X.X - [row].[column]
	
	@Column(length = 50)
	String icon;				// account icon 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	@OrderBy("date DESC, id ASC")
    List<Entry> entries;        // entries


	public Account(Integer id) {
		super(id);
	}

}

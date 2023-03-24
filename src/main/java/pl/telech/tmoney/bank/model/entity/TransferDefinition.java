package pl.telech.tmoney.bank.model.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Definition of transfer amount from one account to another.
 */
@Data
@Entity
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "transfer_definition", schema = "bank")
public class TransferDefinition extends AbstractEntity {
			
	@ManyToOne(optional = false)
    @JoinColumn(name = "sourceAccountId")
	Account sourceAccount;			// source account
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "destinationAccountId")
	Account destinationAccount;		// destination account
		
	@ManyToOne(optional = false)
    @JoinColumn(name = "categoryId")
	Category category;				// category
	
	@Column(length = 100, nullable = false)
	String name;					// entry name
	
	@Column(length = 255)
	String description;				// entry description
		
}

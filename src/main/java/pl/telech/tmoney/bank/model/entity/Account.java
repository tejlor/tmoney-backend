package pl.telech.tmoney.bank.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * Account (not necessarily bank). Physical or not place holding money.
 */
@Data
@Entity
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "account", schema = "bank")
public class Account extends AbstractEntity {
	
	public static final String SUMMARY_CODE = "SUMMARY";
	
	@Column(length = 10, nullable = false)
	String code;				// technical code
	
	@Column(length = 100, nullable = false)
	String name;				// account name
	
	@Column(nullable = false)
	boolean active;				// active accounts are visible at front and in reports
	
	@Column(nullable = false)
	boolean includeInSummary;	// entries should be included in summary table and balance (not for ike / ikze)
	
	@Column(length = 6) 
	String color;				// color in 000000 format
	
	@Column(length = 6) 
	String lightColor;			// light color for backgorund in 000000 format
	
	@Column(length = 6) 
	String darkColor;			// dark color for text in 000000 format
	
	@Column(length = 3)
	String orderNo;				// account order at front in format X.X - [row].[column]
	
	@Column(length = 50)
	String icon;				// font awesome account icon 
	
	@Column(columnDefinition = "TEXT")
	String logo;				// logo jpg file in base64
		
	
	public int getLightColorInt() {
		return Integer.parseInt(lightColor, 16);
	}

}

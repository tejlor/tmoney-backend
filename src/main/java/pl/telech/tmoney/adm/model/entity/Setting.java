package pl.telech.tmoney.adm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * System setting.
 */
@Data
@Entity
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "setting", schema = "adm")
public class Setting extends AbstractEntity {
	
	@Column(length = 32, nullable = false)
	String name;					// setting name
	
	@Column(length = 255)
	String value;					// setting value
	
}

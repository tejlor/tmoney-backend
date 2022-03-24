package pl.telech.tmoney.adm.model.entity;

import static lombok.AccessLevel.PRIVATE;

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
 * System setting.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@FieldNameConstants
@Table(name = "setting", schema = "adm")
public class Setting extends AbstractEntity {
	
	@Column(length = 32, nullable = false)
	String name;					// setting name
	
	@Column(length = 255)
	String value;					// setting value
	

	public Setting(Integer id) {
		super(id);
	}
}

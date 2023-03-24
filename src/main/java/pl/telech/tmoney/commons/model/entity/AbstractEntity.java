package pl.telech.tmoney.commons.model.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/*
 * Base class for all enity classes.
 */
@Data
@FieldNameConstants
@MappedSuperclass
public abstract class AbstractEntity implements Serializable, Comparable<AbstractEntity> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;		// id of object
	
	@Override
	public int compareTo(AbstractEntity other) {
		return id - other.id;
	}
	
}

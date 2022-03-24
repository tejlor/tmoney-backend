package pl.telech.tmoney.commons.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import javassist.Modifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.utils.TConstants;

/*
 * Base class for all enity classes.
 */
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@FieldNameConstants
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;									// id of object
	
	public AbstractEntity(Integer id) {
		this.id = id;
	}
	
	public String toFullString(int depth) {
		if(depth == 2)
			return "...";
		
		StringBuilder sb = new StringBuilder();
		List<Field> fields = new ArrayList<Field>();
		
		if(this.getClass().getSuperclass() != null) { 
			fields.addAll(Arrays.asList(this.getClass().getSuperclass().getDeclaredFields()));  // from super class
		}
		fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));						// from this class
		
		for (Field field : fields) {
			try {
				if(Modifier.isStatic(field.getModifiers()))
					continue;
				
				field.setAccessible(true);
				Object obj = field.get(this);
				if (obj instanceof AbstractEntity) {
					sb.append(TConstants.INDENT[depth] + field.getName() + ":\n");
					sb.append(((AbstractEntity) obj).toFullString(depth + 1));
				}
				else if(obj instanceof List){
					sb.append(TConstants.INDENT[depth] + field.getName() + ": ");
					sb.append("list of " + ((List<?>)obj).size() + " elements\n");
				}
				else {
					sb.append(TConstants.INDENT[depth] + field.getName() + ": " + obj + "\n");
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return id.intValue();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (this.getClass() != other.getClass())
			return false;

		AbstractEntity otherEntity = (AbstractEntity) other;
		return id.equals(otherEntity.id);
	}
}

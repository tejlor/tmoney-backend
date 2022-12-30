package pl.telech.tmoney.commons.model.dto;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.interfaces.Loggable;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PROTECTED)
public abstract class AbstractDto implements Loggable {
	
	Integer id; 
}

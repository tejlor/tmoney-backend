package pl.telech.tmoney.commons.model.dto;

import lombok.Getter;
import lombok.Setter;
import pl.telech.tmoney.commons.model.interfaces.Loggable;

@Getter @Setter
public abstract class AbstractDto implements Loggable {
	
	Integer id; 
}

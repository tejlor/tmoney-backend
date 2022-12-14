package pl.telech.tmoney.bank.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto extends AbstractDto implements Comparable<CategoryDto> {
	String name;	
	Integer account;			
	Boolean report;				
	String defaultName;			
	BigDecimal defaultAmount;	
	String defaultDescription;	

	@Override
	public int compareTo(CategoryDto other) {
		return name.compareToIgnoreCase(other.name);
	}
}
package pl.telech.tmoney.bank.model.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto extends AbstractDto implements Comparable<CategoryDto> {
	
	@Size(max = 100)
	String name;	
	
	@NotNull
	List<Integer> accountIds;			
	
	@NotNull
	Boolean report;
	
	@Size(max = 100)
	String defaultName;			
	
	BigDecimal defaultAmount;	
	
	@Size(max = 255)
	String defaultDescription;	
	

	@Override
	public int compareTo(CategoryDto other) {
		return name.compareToIgnoreCase(other.name);
	}
}
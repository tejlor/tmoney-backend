package pl.telech.tmoney.bank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class EntryDto extends AbstractDto {
	
	AccountDto account;			
	
	LocalDate date;				
	
	CategoryDto category;	
	
	@Size(max = 100)
	String name;				
	
	BigDecimal amount;
	
	@Size(max = 255)
	String description;			

	BigDecimal balance;
	
	BigDecimal balanceOverall;
}
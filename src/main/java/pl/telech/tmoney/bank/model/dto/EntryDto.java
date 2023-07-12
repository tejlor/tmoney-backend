package pl.telech.tmoney.bank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class EntryDto extends AbstractDto {
	
	@NotNull
	AccountDto account;			
	
	@NotNull
	@PastOrPresent
	LocalDate date;				
	
	@NotNull
	CategoryDto category;	
	
	@Size(min = 1, max = 100)
	String name;				
	
	@NotNull
	BigDecimal amount;
	
	@Size(max = 255)
	String description;			

	BigDecimal balance;
	
	BigDecimal balanceOverall;
}
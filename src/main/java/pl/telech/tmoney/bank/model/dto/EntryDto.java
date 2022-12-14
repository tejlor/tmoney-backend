package pl.telech.tmoney.bank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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
	String name;				
	BigDecimal amount;			 
	String description;			
	BigDecimal balance;
	BigDecimal balanceOverall;
}
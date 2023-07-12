package pl.telech.tmoney.bank.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class TransferDefinitionDto extends AbstractDto {
	
	@NotNull
	AccountDto sourceAccount;	
	
	@NotNull
	AccountDto destinationAccount;
	
	@NotNull
	CategoryDto category;
	
	@NotBlank
	@Size(min = 1, max = 100)
	String name;						 
	
	@Size(max = 255)
	String description;			
}
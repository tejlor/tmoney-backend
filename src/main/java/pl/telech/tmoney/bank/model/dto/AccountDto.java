package pl.telech.tmoney.bank.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class AccountDto extends AbstractDto {
	
	@NotBlank
	@Size(max = 10)
	@Pattern(regexp = "[A-Z_]+")
	String code;
	
	@NotBlank
	@Size(max = 100)
	String name;
	
	boolean active;
	
	boolean includeInSummary;
	
	@Valid
	CategoryDto balancingCategory;
	
	@Size(max = 6)
	@Pattern(regexp = "[A-Z0-9]{6}")
	String color;
	
	@Size(max = 6)
	@Pattern(regexp = "[A-Z0-9]{6}")
	String lightColor;
	
	@Size(max = 6)
	@Pattern(regexp = "[A-Z0-9]{6}")
	String darkColor;
	
	@Size(max = 3)
	@Pattern(regexp = "[1-9]\\.[1-9]")
	String orderNo;
	
	@Size(max = 50)
	String icon;
	
	@Size(max = 10000)
	@Pattern(regexp = "data:image/jpeg;base64.+")
	String logo;
}
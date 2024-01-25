package pl.telech.tmoney.adm.model.dto;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class UserDto extends AbstractDto {
	
	@Size(max = 32)
	String firstName;
	
	@Size(max = 32)
	String lastName;
	
	@Size(max = 64)
	String email;
}

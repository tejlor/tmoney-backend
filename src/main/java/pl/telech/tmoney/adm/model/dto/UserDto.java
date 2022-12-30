package pl.telech.tmoney.adm.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class UserDto extends AbstractDto {
	String firstName;
	String lastName;
	String email;
}

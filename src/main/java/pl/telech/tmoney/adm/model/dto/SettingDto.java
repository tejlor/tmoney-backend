package pl.telech.tmoney.adm.model.dto;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class SettingDto extends AbstractDto {
	
	@Size(max = 32)
	String name;
	
	@Size(max = 255)
	String value;
}

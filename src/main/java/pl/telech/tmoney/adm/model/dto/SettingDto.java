package pl.telech.tmoney.adm.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class SettingDto extends AbstractDto {
	String name;
	String value;
}

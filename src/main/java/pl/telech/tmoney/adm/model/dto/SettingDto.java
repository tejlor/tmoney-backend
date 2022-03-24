package pl.telech.tmoney.adm.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SettingDto extends AbstractDto {

	String name;
	String value;
	
	
	public SettingDto(Setting model){
		super(model);
	}

	@Override
	public Setting toModel() {
		var setting = new Setting();
		fillModel(setting);
		return setting;
	}
	
	public static List<SettingDto> toDtoList(List<Setting> list){
		return toDtoList(Setting.class, SettingDto.class, list);
	}
}

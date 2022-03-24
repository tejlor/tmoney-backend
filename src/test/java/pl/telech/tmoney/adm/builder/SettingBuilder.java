package pl.telech.tmoney.adm.builder;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class SettingBuilder extends AbstractBuilder<Setting> {
	
	String name = "PARAM_1";
	String value = "true";
	
	@Override
	public Setting build() {
		var obj = new Setting();
		super.fill(obj);
		obj.setName(name);
		obj.setValue(value);
		return obj;	
	}
}

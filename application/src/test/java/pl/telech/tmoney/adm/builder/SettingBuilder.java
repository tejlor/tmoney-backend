package pl.telech.tmoney.adm.builder;

import lombok.Setter;
import lombok.experimental.Accessors;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.builder.AbstractBuilder;

@Setter
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

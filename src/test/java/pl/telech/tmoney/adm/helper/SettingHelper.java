package pl.telech.tmoney.adm.helper;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.adm.builder.SettingBuilder;
import pl.telech.tmoney.adm.model.entity.Setting;

@Component
public class SettingHelper {

	@Autowired
	EntityManager entityManager;
	
	
	@Transactional
	public Setting save(String name, Object value) {
		return new SettingBuilder()
			.name(name)
			.value(value.toString())
			.save(entityManager);
	}
}

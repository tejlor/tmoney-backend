package pl.telech.tmoney.bank.helper;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.telech.tmoney.bank.builder.CategoryBuilder;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.enums.Mode;

@Component
public class CategoryHelper {

	@Autowired
	EntityManager entityManager;
	
	
	public void assertEqual(CategoryDto dto, Category model, Mode mode) {
		if(mode != Mode.CREATE) {
			assertThat(dto.getId()).isEqualTo(model.getId());
		}
		assertThat(dto.getName()).isEqualTo(model.getName());
		assertThat(dto.getAccount()).isEqualTo(model.getAccount());
		assertThat(dto.getReport()).isEqualTo(model.getReport());
		assertThat(dto.getDefaultName()).isEqualTo(model.getDefaultName());
		assertThat(dto.getDefaultAmount()).isEqualByComparingTo(model.getDefaultAmount());
		assertThat(dto.getDefaultDescription()).isEqualTo(model.getDefaultDescription());
	}
	
	public Category save(String name) {
		return new CategoryBuilder()
			.name(name)
			.save(entityManager);
	}
	
	public Category build(String name) {
		return new CategoryBuilder()
			.name(name)
			.build();
	}
}

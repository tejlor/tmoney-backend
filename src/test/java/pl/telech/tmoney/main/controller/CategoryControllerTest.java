package pl.telech.tmoney.main.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.main.builder.CategoryBuilder;
import pl.telech.tmoney.main.model.dto.CategoryDto;
import pl.telech.tmoney.main.model.entity.Category;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class CategoryControllerTest extends BaseTest {

	@Autowired
	CategoryController controller;
	
	@Test
	@Transactional
	public void getAll() {	
		// given
		Category category0 = setupCategory("Samochód");
		Category category1 = setupCategory("Zakupy");
		Category category2 = setupCategory("Praca");
		flush();
		
		// when
		List<CategoryDto> result = controller.getAll();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertCategory(result.get(0), category0, true);
		assertCategory(result.get(1), category1, true);
		assertCategory(result.get(2), category2, true);
	}
	
	@Test
	@Transactional
	public void getById() {	
		// given
		Category category = setupCategory("Samochód");
		flush();
		
		// when
		CategoryDto result = controller.getById(category.getId());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertCategory(result, category, true);
	}
	
	@Test
	@Transactional
	public void create() {	
		// given
		flush();
		
		// when
		Category category = buildCategory("Samochód");
		CategoryDto result = controller.create(new CategoryDto(category));	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertCategory(result, category, false);
	}
	
	@Test
	@Transactional
	public void update() {	
		// given
		Category category = setupCategory("Samochód");
		flush();
		
		// when
		CategoryDto dto = new CategoryDto(category);
		dto.setDefaultDescription("Nowy opis");
		CategoryDto result = controller.update(category.getId(), dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		Category updatedCategory = load(Category.class, category.getId());
		assertCategory(result, updatedCategory, true);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertCategory(CategoryDto dto, Category model, boolean withId) {
		if(withId) {
			assertThat(dto.getId()).isEqualTo(model.getId());
		}
		assertThat(dto.getName()).isEqualTo(model.getName());
		assertThat(dto.getAccount()).isEqualTo(model.getAccount());
		assertThat(dto.getReport()).isEqualTo(model.getReport());
		assertThat(dto.getDefaultName()).isEqualTo(model.getDefaultName());
		assertThat(dto.getDefaultAmount()).isEqualByComparingTo(model.getDefaultAmount());
		assertThat(dto.getDefaultDescription()).isEqualTo(model.getDefaultDescription());
	}
	
	private Category setupCategory(String name) {
		return new CategoryBuilder()
			.name(name)
			.save(entityManager);
	}
	
	private Category buildCategory(String name) {
		return new CategoryBuilder()
			.name(name)
			.build();
	}
	
}

package pl.telech.tmoney.bank.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.asserts.EntityAssert;
import pl.telech.tmoney.commons.enums.Mode;

public class CategoryAssert extends EntityAssert<Category, CategoryDto> {
	
	private CategoryAssert(CategoryDto result) {
		super(result);
	}
	
	public static CategoryAssert assertThatDto(CategoryDto result) {
		return new CategoryAssert(result);
	}

	@Override
	protected void compare(CategoryDto dto, Mode mode) {
		if(mode == Mode.GET) {
			assertThat(dto.getId()).isEqualTo(entity.getId());
		}
		assertThat(dto.getName()).isEqualTo(entity.getName());
		assertThat(dto.getAccount()).isEqualTo(entity.getAccount());
		assertThat(dto.getReport()).isEqualTo(entity.getReport());
		assertThat(dto.getDefaultName()).isEqualTo(entity.getDefaultName());
		assertThat(dto.getDefaultAmount()).isEqualTo(entity.getDefaultAmount());
		assertThat(dto.getDefaultDescription()).isEqualTo(entity.getDefaultDescription());
	}		
}

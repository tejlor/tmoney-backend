package pl.telech.tmoney.bank.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import pl.telech.tmoney.bank.asserts.CategoryAssert;
import pl.telech.tmoney.bank.builder.CategoryBuilder;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;

@RunWith(SpringRunner.class)
@Import({CategoryBuilder.class, CategoryMapperImpl.class})
public class CategoryMapperTest {

	@Autowired
	CategoryBuilder builder;

	@Autowired
	CategoryMapper mapper;

	@Test
	public void testToDto() {
		// given
		Category entity = builder.id(1).build();

		// when
		CategoryDto result = mapper.toDto(entity);

		// then
		assertThat(result).isNotNull();
		CategoryAssert.assertThatDto(result).isMappedFrom(entity);
	}

	@Test
	public void testToDtoList() {
		// given
		List<Category> list = List.of(builder.id(1).build());

		// when
		List<CategoryDto> result = mapper.toDtoList(list);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSameSizeAs(list);
		CategoryAssert.assertThatDto(result.get(0)).isMappedFrom(list.get(0));
	}
	
	@Test
	public void testToEntity() {
		// given
		CategoryDto dto = mapper.toDto(builder.id(1).build());

		// when
		Category result = mapper.toEntity(dto);

		// then
		assertThat(result).isNotNull();
		CategoryAssert.assertThatDto(dto).isMappedFrom(result);
	}
	
	@Test
	public void testToNewEntity() {
		// given
		CategoryDto dto = mapper.toDto(builder.id(1).build());

		// when
		Category result = mapper.toNewEntity(dto);

		// then
		assertThat(result).isNotNull();
		CategoryAssert.assertThatDto(dto).creates(result);
	}
	
	@Test
	public void testUpdate() {
		// given
		CategoryDto dto = mapper.toDto(builder.id(1).build());

		// when
		Category updatedEntity = new Category();
		Category result = mapper.update(dto, updatedEntity);

		// then
		assertThat(result).isSameAs(updatedEntity);
		CategoryAssert.assertThatDto(dto).updates(result);
	}
}

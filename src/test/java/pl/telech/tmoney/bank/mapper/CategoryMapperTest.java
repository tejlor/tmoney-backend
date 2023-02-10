package pl.telech.tmoney.bank.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.asserts.CategoryAssert;
import pl.telech.tmoney.bank.builder.CategoryBuilder;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperTest;

@ExtendWith(SpringExtension.class)
@Import({CategoryBuilder.class, CategoryMapperImpl.class})
public class CategoryMapperTest extends EntityMapperTest<Category, CategoryDto> {

	@Autowired
	CategoryBuilder builder;

	@Autowired
	CategoryMapper mapper;
	
	@Override
	protected AbstractBuilder<Category> getBuilder() {
		return builder;
	}

	@Override
	protected EntityMapper<Category, CategoryDto> getMapper() {
		return mapper;
	}

	@Override
	protected Class<CategoryAssert> getAssertionsClass() {
		return CategoryAssert.class;
	}
}

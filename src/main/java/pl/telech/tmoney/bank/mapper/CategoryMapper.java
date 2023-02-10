package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.*;

import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface CategoryMapper extends EntityMapper<Category, CategoryDto> {
	
	CategoryDto toDto(Category entity);
	
	List<CategoryDto> toDtoList(Collection<Category> entities);
	
	@Named("toEntity")
	Category toEntity(CategoryDto dto);
	
	@Named("create")
	@InheritConfiguration(name = "create")
	Category create(CategoryDto dto);
	
	@InheritConfiguration(name = "update")
	Category update(@MappingTarget Category entity, CategoryDto dto);
}

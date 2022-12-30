package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface CategoryMapper {
	
	CategoryDto toDto(Category entity);
	
	List<CategoryDto> toDtoList(Collection<Category> entity);
	
	@Named("toEntity")
	Category toEntity(CategoryDto dto);
	
	@Named("toNewEntity")
	@InheritConfiguration(name = "toNewEntity")
	Category toNewEntity(CategoryDto dto);
	
	@InheritConfiguration(name = "update")
	Category update(CategoryDto dto, @MappingTarget Category entity);
}

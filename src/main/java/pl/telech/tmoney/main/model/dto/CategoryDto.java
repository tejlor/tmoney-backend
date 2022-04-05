package pl.telech.tmoney.main.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.main.model.entity.Category;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CategoryDto extends AbstractDto {

	String name;	
	Integer account;			
	Boolean report;				
	String defaultName;			
	BigDecimal defaultAmount;	
	String defaultDescription;	
	
	
	public CategoryDto(Category model){
		super(model);	
	}

	@Override
	public Category toModel() {
		var model = new Category();
		fillModel(model);
		return model;
	}
	
	public static List<CategoryDto> toDtoList(List<Category> list){
		return toDtoList(Category.class, CategoryDto.class, list);
	}
}
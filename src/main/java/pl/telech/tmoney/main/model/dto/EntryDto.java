package pl.telech.tmoney.main.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.main.model.entity.Account;
import pl.telech.tmoney.main.model.entity.Category;
import pl.telech.tmoney.main.model.entity.Entry;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EntryDto extends AbstractDto {

    Integer accountId;
	Account account;			
	LocalDate date;				
    Integer categoryId;
	Category category;			
	String name;				
	BigDecimal amount;			 
	String description;			
	BigDecimal balance;
	BigDecimal balanceOverAll;
	
	
	public EntryDto(Entry model){
		super(model);	
	}

	@Override
	public Entry toModel() {
		var model = new Entry();
		fillModel(model);
		return model;
	}
	
	public static List<EntryDto> toDtoList(List<Entry> list){
		return toDtoList(Entry.class, EntryDto.class, list);
	}
}
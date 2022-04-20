package pl.telech.tmoney.bank.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EntryDto extends AbstractDto {

	AccountDto account;			
	LocalDate date;				
	CategoryDto category;			
	String name;				
	BigDecimal amount;			 
	String description;			
	BigDecimal balance;
	BigDecimal balanceOverall;
	
	
	public EntryDto(Entry model){
		super(model);	
		account = new AccountDto(model.getAccount());
		category = new CategoryDto(model.getCategory());
	}

	@Override
	public Entry toModel() {
		var model = new Entry();
		fillModel(model);
		
		if(account != null) {
			model.setAccount(new Account(account.getId()));
		}
		if(category != null) {
			model.setCategory(new Category(category.getId()));
		}
		
		return model;
	}
	
	public static List<EntryDto> toDtoList(List<Entry> list){
		return toDtoList(Entry.class, EntryDto.class, list);
	}
}
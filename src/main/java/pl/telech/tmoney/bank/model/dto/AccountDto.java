package pl.telech.tmoney.bank.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AccountDto extends AbstractDto {

	String code;
	String name;
	Boolean active;
	String color;
	Integer orderNo;
	
	
	public AccountDto(Account model){
		super(model);	
	}

	@Override
	public Account toModel() {
		var model = new Account();
		fillModel(model);
		return model;
	}
	
	public static List<AccountDto> toDtoList(List<Account> list){
		return toDtoList(Account.class, AccountDto.class, list);
	}
}
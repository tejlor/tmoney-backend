package pl.telech.tmoney.bank.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.dto.AbstractDto;

@Getter @Setter
@NoArgsConstructor
public class AccountDto extends AbstractDto {
	String code;
	String name;
	Boolean active;
	String color;
	String lightColor;
	String darkColor;
	String orderNo;
	String icon;
}
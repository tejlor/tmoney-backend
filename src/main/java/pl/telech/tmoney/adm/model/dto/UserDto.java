package pl.telech.tmoney.adm.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.model.dto.AbstractDto;


@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserDto extends AbstractDto {

	String firstName;
	String lastName;
	String email;
	
	
	public UserDto(User model){
		super(model);	
	}

	@Override
	public User toModel() {
		User user = new User();
		fillModel(user);
		return user;
	}
	
	public static List<UserDto> toDtoList(List<User> list){
		return toDtoList(User.class, UserDto.class, list);
	}
}

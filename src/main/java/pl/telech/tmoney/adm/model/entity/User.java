package pl.telech.tmoney.adm.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.util.*;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * System user.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Table(name = "user", schema = "adm")
public class User extends AbstractEntity implements UserDetails {
			
	@Column(length = 32, nullable = false)
	String firstName;				// first name
	
	@Column(length = 32, nullable = false)
	String lastName;				// last name
	
	@Column(length = 64, nullable = false, unique = true)
	String email;					// email address
	
	@Column(length = 40, nullable = false)
	String password;				// password (hash SHA1)
				

	public User(Integer id) {
		super(id);
	}

	public String calcShortName(){
        return firstName.substring(0, 1) + "." + lastName;
	}
	
	// UserDetails
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();
		/*
		if(roleXXX)
			list.add(new SimpleGrantedAuthority("ROLE_XXX));
		*/
		return list;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

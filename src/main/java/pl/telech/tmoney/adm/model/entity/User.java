package pl.telech.tmoney.adm.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

/*
 * System user.
 */
@Data
@Entity
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

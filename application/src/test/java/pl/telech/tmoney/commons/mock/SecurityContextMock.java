package pl.telech.tmoney.commons.mock;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.adm.model.entity.User;

@RequiredArgsConstructor(staticName = "withUser")
public class SecurityContextMock implements SecurityContext {
		
	final User user;	
	
	@Override
	public void setAuthentication(Authentication authentication) {}
	
	@Override
	public Authentication getAuthentication() {
		return AuthenticationMock.withUser(user);
	}
}

@RequiredArgsConstructor(staticName = "withUser")
class AuthenticationMock implements Authentication {
	
	final User user;
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		
	}
	
	@Override
	public boolean isAuthenticated() {
		return false;
	}
	
	@Override
	public Object getPrincipal() {
		return user;
	}
	
	@Override
	public Object getDetails() {
		return null;
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
}

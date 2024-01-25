package pl.telech.tmoney.commons.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Encoder using in authorisation oAuth 2.0.
 */
public class Sha1PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return TUtils.sha1(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if(encodedPassword == null)
			return false;
		
		return TUtils.sha1(rawPassword.toString()).compareToIgnoreCase(encodedPassword) == 0;
	}
}

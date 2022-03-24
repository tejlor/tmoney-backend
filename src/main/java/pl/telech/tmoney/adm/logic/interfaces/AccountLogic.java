package pl.telech.tmoney.adm.logic.interfaces;


import org.springframework.security.core.userdetails.UserDetails;

import pl.telech.tmoney.adm.model.entity.User;

public interface AccountLogic {

	User getCurrentUser();
	User loadCurrentUser();
	UserDetails loadUserByUsername(String username);
}

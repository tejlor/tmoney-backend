package pl.telech.tmoney.adm.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.adm.dao.UserDAO;
import pl.telech.tmoney.adm.logic.interfaces.UserLogic;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class UserLogicImpl extends AbstractLogicImpl<User> implements UserLogic, UserDetailsService {

	@Value("${tmoney.auth.clientName}")
	String clientName;
	@Value("${tmoney.auth.clientPass}")
	String clientPass;
	
	UserDAO dao;
	
	
	public UserLogicImpl(UserDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	/*
	 * Gets logged user from session.
	 * Object isn't connected to Hibernate session and doesn't have sub-objects.
	 */
	@Override
	public User getCurrentUser() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication.getPrincipal();

			if (principal instanceof User) {
				log.debug("get " + ((User)principal).toFullString(0));
				return (User) principal;
			}
			else {
				//return null;
				return new User(1); 
			}
		}
		catch (Exception e) {
			log.warn("Principal is null: " + e.getMessage());
			//return null;
			return new User(1); 
		}
	}
	
	@Override
	public User loadCurrentUser() {
		return dao.findById(getCurrentUser().getId()).get();
	}
	
	// UserDetailsService
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = dao.findByEmail(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
		
		log.debug("load " + user.toFullString(0));
		return user;
	}

	// #################################### PRIVATE ###################################################################################
	
}

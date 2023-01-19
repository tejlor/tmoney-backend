package pl.telech.tmoney.adm;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.adm.builder.UserBuilder;
import pl.telech.tmoney.adm.model.entity.User;

@Component
public class UserHelper {

	@Autowired
	EntityManager entityManager;
	
	
	@Transactional
	public User save(String email) {
		return new UserBuilder()
			.email(email)
			.save(entityManager);
	}
}

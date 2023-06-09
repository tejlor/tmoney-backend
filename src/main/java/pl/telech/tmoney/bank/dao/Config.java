package pl.telech.tmoney.bank.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import pl.telech.tmoney.bank.model.entity.Account;

@Configuration
public class Config {

	@Autowired
	EntityManager manager;
	
	@Bean
	public JpaEntityInformation<Account, ?> acount(){
		return JpaEntityInformationSupport.getEntityInformation(Account.class, manager);
	}
 }

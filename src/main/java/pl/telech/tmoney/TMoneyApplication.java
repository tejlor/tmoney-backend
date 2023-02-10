package pl.telech.tmoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import pl.telech.tmoney.commons.dao.DAOImpl;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "pl.telech.tmoney.*.dao", repositoryBaseClass = DAOImpl.class)
public class TMoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TMoneyApplication.class, args);
	}

}
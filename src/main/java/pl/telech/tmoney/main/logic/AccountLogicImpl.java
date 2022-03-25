package pl.telech.tmoney.main.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.main.dao.AccountDAO;
import pl.telech.tmoney.main.logic.interfaces.AccountLogic;
import pl.telech.tmoney.main.model.entity.Account;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class AccountLogicImpl extends AbstractLogicImpl<Account> implements AccountLogic {
	
	AccountDAO dao;
	
	public AccountLogicImpl(AccountDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	// ################################### PRIVATE #########################################################################
	
}

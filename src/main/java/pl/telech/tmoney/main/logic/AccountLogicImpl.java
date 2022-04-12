package pl.telech.tmoney.main.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.commons.utils.TUtils;
import pl.telech.tmoney.main.dao.AccountDAO;
import pl.telech.tmoney.main.logic.interfaces.AccountLogic;
import pl.telech.tmoney.main.model.entity.Account;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class AccountLogicImpl extends AbstractLogicImpl<Account> implements AccountLogic {
	
	public AccountLogicImpl(AccountDAO dao) {
		super(dao);
	}
	
	@Override
	public Account create(Account _account) {
		validate(_account);
		
		var account = new Account();
		account.setCode(_account.getCode());
		account.setName(_account.getName());
		account.setActive(_account.getActive());
		account.setColor(_account.getColor());
		account.setOrderNo(_account.getOrderNo());
		
		return save(account);
	}
	
	@Override
	public Account update(int id, Account _account) {
		validate(_account);
		
		Account account = loadById(id);
		TUtils.assertEntityExists(account);
				
		account.setName(_account.getName());
		account.setActive(_account.getActive());
		account.setColor(_account.getColor());
		account.setOrderNo(_account.getOrderNo());
		
		return save(account);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void validate(Account account) {
		
	}
}

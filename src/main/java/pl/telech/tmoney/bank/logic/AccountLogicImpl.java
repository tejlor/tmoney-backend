package pl.telech.tmoney.bank.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.dao.AccountDAO;
import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class AccountLogicImpl extends AbstractLogicImpl<Account> implements AccountLogic {
	
	AccountDAO dao;
	
	@Autowired
	EntryLogic entryLogic;
	
	public AccountLogicImpl(AccountDAO dao) {
		super(dao);
		this.dao = dao;
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
	
	@Override
	public List<Pair<Account, Entry>> getAccountSummaryList() {
		return dao.findActive().stream()
			.map(account -> Pair.of(account, entryLogic.loadLastByAccount(account.getId())))
			.collect(Collectors.toList());
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void validate(Account account) {
		
	}
}

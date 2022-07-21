package pl.telech.tmoney.bank.logic;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class EntryLogicImpl extends AbstractLogicImpl<Entry> implements EntryLogic {
	
	EntryDAO dao;
	
	@Autowired
	AccountLogic accountLogic;
	
	public EntryLogicImpl(EntryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public Pair<List<Entry>, Integer> loadAll(String accountCode, TableParams params) {
		Integer accountId = null;
		
		if(accountCode != null) {
			Account account = accountLogic.loadByCode(accountCode);
			if(account == null) {
				throw new TMoneyException("Account with code " + accountCode + " doesn't exist");
			}
			accountId = account.getId();
		}
		
		return dao.findTableByAccountId(accountId, params);
	}
	
	@Override
	public List<Entry> loadByCategoryId(int categoryId) {
		return dao.findByCategoryId(categoryId);
	}
	
	@Override
	public Entry create(Entry _entry) {
		validate(_entry);
		
		var entry = new Entry();	
		entry.setAccount(_entry.getAccount());
		entry.setDate(_entry.getDate());
		entry.setCategory(_entry.getCategory());
		entry.setName(_entry.getName());
		entry.setAmount(_entry.getAmount());
		entry.setDescription(_entry.getDescription());
		calculateAndFillBalances(entry);
		
		return save(entry);
	}
	
	@Override
	public Entry update(int id, Entry _entry) {
		validate(_entry);
		
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
		
		entry.setAccount(_entry.getAccount());
		entry.setDate(_entry.getDate());
		entry.setCategory(_entry.getCategory());
		entry.setName(_entry.getName());
		entry.setAmount(_entry.getAmount());
		entry.setDescription(_entry.getDescription());
		
		return save(entry);
	}
	
	@Override
	public void delete(int id) {
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
				
		delete(entry);
	}
	
	@Override
	public Entry loadLastByAccount(int accountId) {
		return dao.findLastByAccountBeforeDate(accountId, LocalDate.now());
	}
	
	@Override
	public void updateBalances() {
		dao.updateBalances();
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void validate(Entry entry) {
		
	}
	
	private void calculateAndFillBalances(Entry entry) {
		Entry lastAccountEntry = dao.findLastByAccountBeforeDate(entry.getAccountId(), entry.getDate());
		entry.setBalance(lastAccountEntry.getBalance().add(entry.getAmount()));
		
		Entry lastEntry = dao.findLastBeforeDate(entry.getDate());
		entry.setBalanceOverall(lastEntry.getBalanceOverall().add(entry.getAmount()));
	}
		
}

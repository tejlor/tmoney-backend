package pl.telech.tmoney.bank.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractLogicImpl;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
public class EntryLogicImpl extends AbstractLogicImpl<Entry> implements EntryLogic {
	
	EntryDAO dao;
	
	@Value("${tmoney.environment}")
	String environment;
	
	@Autowired
	AccountLogic accountLogic;
	
	@Autowired
	EntryMapper mapper;
	
	public EntryLogicImpl(EntryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public List<Entry> loadAll(String accountCode) {
		Integer accountId = null;
		
		if(accountCode != null) {
			Account account = accountLogic.loadByCode(accountCode);
			if(account == null) {
				throw new TMoneyException("Account with code " + accountCode + " doesn't exist");
			}
			accountId = account.getId();
		}
		
		return dao.findByAccountId(accountId);
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
	public Entry create(EntryDto entryDto) {
		Entry entry = mapper.toNewEntity(entryDto);
		calculateAndFillBalances(entry);
		
		entry = saveAndFlush(entry);
		updateBalances();
		reload(entry);
		return entry;
	}
	
	@Override
	public Entry update(int id, EntryDto entryDto) {
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
		mapper.update(entryDto, entry);	
		
		entry = saveAndFlush(entry);
		updateBalances();
		reload(entry);
		return entry;
	}
	
	@Override
	public void delete(int id) {
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
				
		delete(entry);
		updateBalances();
	}
	
	@Override
	public Entry loadLastByAccount(int accountId) {
		return dao.findLastByAccountBeforeDate(accountId, LocalDate.now());
	}
	
	@Override
	public void updateBalances() {
		if (!TUtils.isJUnit(environment)) {
			dao.updateBalances();
		}
	}
	
	// ################################### PRIVATE #########################################################################
	
	
	private void calculateAndFillBalances(Entry entry) {
		entry.setBalance(BigDecimal.ZERO);
		entry.setBalanceOverall(BigDecimal.ZERO);
	}
		
}

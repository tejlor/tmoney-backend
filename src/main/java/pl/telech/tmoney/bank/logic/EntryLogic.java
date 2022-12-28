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
public class EntryLogic extends AbstractLogicImpl<Entry> {
	
	EntryDAO dao;
	
	@Value("${tmoney.environment}")
	String environment;
	
	@Autowired
	AccountLogic accountLogic;
	
	@Autowired
	EntryMapper mapper;
	
	public EntryLogic(EntryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
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
	
	public List<Entry> loadByCategoryId(int categoryId) {
		return dao.findByCategoryId(categoryId);
	}
	
	public Entry create(EntryDto entryDto) {
		Entry entry = mapper.toNewEntity(entryDto);
		calculateAndFillBalances(entry);
		
		entry = saveAndFlush(entry);
		updateBalances();
		reload(entry);
		return entry;
	}
	
	public Entry update(int id, EntryDto entryDto) {
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
		mapper.update(entryDto, entry);	
		
		entry = saveAndFlush(entry);
		updateBalances();
		reload(entry);
		return entry;
	}
	
	public void delete(int id) {
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
				
		delete(entry);
		updateBalances();
	}
	
	public Entry loadLastByAccount(int accountId) {
		return dao.findLastByAccountBeforeDate(accountId, LocalDate.now());
	}
	
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

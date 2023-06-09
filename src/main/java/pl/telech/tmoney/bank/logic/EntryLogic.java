package pl.telech.tmoney.bank.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.logic.tag.EntryTagCalculator;
import pl.telech.tmoney.bank.logic.validator.EntryValidator;
import pl.telech.tmoney.bank.mapper.EntryMapper;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.AbstractDomainLogic;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.exception.ValidationException;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class EntryLogic extends AbstractDomainLogic<Entry, EntryDto> {
	
	@Value("${tmoney.environment}")
	final String environment;

	final EntryDAO dao;
	final AccountLogic accountLogic;
	final EntryMapper mapper;
	final EntryTagCalculator entryTagCalculator;
	final EntryValidator validator;
	
	
	@PostConstruct
	public void init() {
		super.dao = dao;
		super.mapper = mapper;
		super.validator = validator;
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
	
	private void replaceTagsWithValues(Entry entry) {
		entry.setDescription(entryTagCalculator.replaceTagsWithValues(entry.getDescription(), entry.getDate()));
	}
	
	public void delete(int id) {
		Entry entry = loadById(id);
		TUtils.assertEntityExists(entry);
				
		delete(entry);
		updateBalances();
	}
	
	public Optional<Entry> loadLastByAccount(int accountId) {
		return dao.findLastByAccountBeforeDate(accountId, LocalDate.now().plusDays(1));
	}
	
	public void updateBalances() {
		if (!TUtils.isJUnit(environment)) {
			dao.updateBalances();
		}
	}
	
	public Entry saveAndRecalculate(Entry entry) {	
		replaceTagsWithValues(entry);
		entry = saveAndFlush(entry);
		updateBalances();
		reload(entry);
		return entry;
	}

	@Override
	public Pair<List<Entry>, Integer> loadTable(TableParams params) {
		// TODO Auto-generated method stub
		return null;
	}
		
}

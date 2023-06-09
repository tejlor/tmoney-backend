package pl.telech.tmoney.bank.logic;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.TransferDefinitionDAO;
import pl.telech.tmoney.bank.logic.validator.TransferDefinitionValidator;
import pl.telech.tmoney.bank.mapper.TransferDefinitionMapper;
import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.logic.AbstractDomainLogic;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferDefinitionLogic extends AbstractDomainLogic<TransferDefinition, TransferDefinitionDto> {
		
	final TransferDefinitionDAO dao;
	final TransferDefinitionMapper mapper;
	final TransferDefinitionValidator validator;

	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
		super.mapper = mapper;
		super.validator = validator;
	}
	
	public Pair<List<TransferDefinition>, Integer> loadTable(TableParams params) {
		return dao.findTable(params);
	}
	
	public void delete(int id) {
		TransferDefinition transferDefinition = loadById(id);		
		TUtils.assertEntityExists(transferDefinition);				
		delete(transferDefinition);
	}
	
}

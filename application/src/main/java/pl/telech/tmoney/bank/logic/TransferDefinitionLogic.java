package pl.telech.tmoney.bank.logic;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.TransferDefinitionDAO;
import pl.telech.tmoney.bank.logic.validator.TransferDefinitionValidator;
import pl.telech.tmoney.bank.mapper.TransferDefinitionMapper;
import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.logic.AbstractLogic;
import pl.telech.tmoney.commons.model.exception.ValidationException;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferDefinitionLogic extends AbstractLogic<TransferDefinition> {
		
	final TransferDefinitionDAO dao;
	final TransferDefinitionMapper mapper;
	final TransferDefinitionValidator validator;

	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
	}
	
	public Pair<List<TransferDefinition>, Integer> loadTable(TableParams params) {
		return dao.findTable(params);
	}
	
	public TransferDefinition create(TransferDefinitionDto dto) {
		TransferDefinition newTransferDefinition = mapper.create(dto);	
		
		Errors errors = new BeanPropertyBindingResult(newTransferDefinition, "Konto");
		validator.validate(newTransferDefinition, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(newTransferDefinition);
	}
	
	public TransferDefinition update(int id, TransferDefinitionDto dto) {
		TransferDefinition transferDefinition = loadById(id);			
		mapper.update(transferDefinition, dto);
		
		Errors errors = new BeanPropertyBindingResult(transferDefinition, "Konto");
		validator.validate(transferDefinition, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(transferDefinition);
	}
	
	public void delete(int id) {
		TransferDefinition transferDefinition = loadById(id);		
		TUtils.assertEntityExists(transferDefinition);				
		delete(transferDefinition);
	}
	
}

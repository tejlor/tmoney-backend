package pl.telech.tmoney.commons.logic;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.dao.DAO;
import pl.telech.tmoney.commons.logic.validator.DomainValidator;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.exception.ValidationException;
import pl.telech.tmoney.commons.model.shared.TableParams;

@FieldDefaults(level = PROTECTED)
public abstract class AbstractDomainLogic<E extends AbstractEntity, T extends AbstractDto> {

	@Autowired
	DAO<E> dao;
	
	@Autowired
	EntityMapper<E,T> mapper;
	
	@Autowired
	DomainValidator<E> validator;
	

	public E loadById(@NonNull Integer id) {
		return dao.findById(id).orElseThrow();
	}

	public List<E> loadAll() {
		return dao.findAll();
	}
	
	public Pair<List<E>, Integer> loadTable(TableParams params) {
		return dao.findTable(params, dao::isLike);
	}
	
	public E create(T dto) {
		E entity = mapper.create(dto);
		
		Errors errors = new BeanPropertyBindingResult(entity, "Entity");
		validator.validate(entity, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(entity);
	}
	
	public E update(int id, T dto) {
		E entity = loadById(id);			
		mapper.update(entity, dto);
		
		Errors errors = new BeanPropertyBindingResult(entity, "Entity");
		validator.validate(entity, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors());
		}
		
		return save(entity);
	}
		
	protected E save(E entity){
		return dao.save(entity);
	}
	
	protected E saveAndFlush(E entity){
		return dao.saveAndFlush(entity);
	}
	
	protected void reload(E entity){
		dao.reload(entity);
	}

	protected void delete(E entity) {
		dao.delete(entity);
	}
}

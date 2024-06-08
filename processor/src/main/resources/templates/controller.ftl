package ${packageName};

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.${moduleName}.logic.${type}Logic;
import pl.telech.tmoney.${moduleName}.mapper.${type}Mapper;
import pl.telech.tmoney.${moduleName}.model.dto.${type}Dto;
import pl.telech.tmoney.${moduleName}.model.entity.${type};
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.shared.TableData;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.commons.utils.TUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("${path}")
public class ${type}BaseController extends AbstractController {

	final ${type}Mapper mapper;
	final ${type}Logic logic;

	<#list methods as method>
		<#switch method.name>
			<#case 'GET_BY_ID'>
				<@getById/>
				<#break>
			<#case 'GET_BY_CODE'>
				<@getByCode/>
				<#break>		
			<#case 'GET_ALL'>
				<@getAll method.args/>
				<#break>		
			<#case 'GET_TABLE'>
				<@getTable/>
				<#break>		
			<#case 'CREATE'>
				<@create/>
				<#break>		
			<#case 'UPDATE'>
				<@update />
				<#break>		
			<#case 'DELETE'>
				<@delete />
				<#break>			
		</#switch>
	</#list>
}

<#macro getById>
	@RequestMapping(value = "/{id:" + ID + "}", method = GET)
	public ${type}Dto getById(
			@PathVariable int id) {
		
		return mapper.toDto(logic.loadById(id));
	}

</#macro>

<#macro getByCode>
	@RequestMapping(value = "/{code:" + CODE + "}", method = GET)
	public ${type}Dto getByCode(
			@PathVariable String code) {
		
		return mapper.toDto(logic.loadByCode(code));
	}

</#macro>

<#macro getAll args>
	@RequestMapping(value = "", method = GET)
	public List<${type}Dto> getAll(
		<#list args as arg>
			@RequestParam(defaultValue = "${arg.defaultValue}") ${arg.type} ${arg.name}<#sep>,</#sep>
		</#list>
	) {	
		return mapper.toDtoList(logic.loadAll(<#list args as arg>${arg.name}<#sep>, </#sep></#list>));
	}

</#macro>

<#macro getTable>
	@RequestMapping(value = "/table", method = GET)
	public TableData<${type}Dto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy) {
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy);		
		Pair<List<${type}>, Integer> result = logic.loadTable(tableParams); 	
		var table = new TableData<${type}Dto>(tableParams);
		table.setRows(mapper.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}

</#macro>

<#macro create>
	@RequestMapping(value = "", method = POST)
	public ${type}Dto create(
			@RequestBody @Valid ${type}Dto dto) {
		
		return mapper.toDto(logic.create(dto));
	}

</#macro>

<#macro update>
	@RequestMapping(value = "/{id:" + ID + "}", method = PUT)
	public ${type}Dto update(
			@PathVariable int id,
			@RequestBody @Valid ${type}Dto dto) {
		
		TUtils.assertDtoId(id, dto);
		return mapper.toDto(logic.update(id, dto));
	}

</#macro>

<#macro delete>
	@RequestMapping(value = "/{id:" + ID + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
			@PathVariable int id) {
		
		logic.delete(id);
	}

</#macro>

package pl.telech.tmoney.adm.logic;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.adm.dao.SettingDAO;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.logic.AbstractDomainLogic;
import pl.telech.tmoney.commons.model.shared.TableParams;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SettingLogic extends AbstractDomainLogic<Setting, SettingDto> {
	
	final SettingDAO dao;
	
	@PostConstruct
	public void init() {
		super.dao = this.dao;
	}
	

	public int loadIntValue(String name) {
		int value = 0;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = Integer.parseInt(valueStr);
			}
			catch(NumberFormatException e) {
				log.warn("Cannot parse int setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	public BigDecimal loadDecimalValue(String name) {
		BigDecimal value = BigDecimal.ZERO;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = new BigDecimal(valueStr);
			}
			catch(NumberFormatException e) {
				log.warn("Cannot parse decimal setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	public boolean loadBoolValue(String name) {
		boolean value = false;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			value = Boolean.parseBoolean(valueStr);
		}
		
		return value;
	}
	
	public String loadStringValue(String name) {
		return loadValue(name);
	}
	
	private String loadValue(String name) {
		Setting setting = dao.findByName(name);
		if(setting == null)
			return null;
		
		return setting.getValue();
	}


	@Override
	public Pair<List<Setting>, Integer> loadTable(TableParams params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

package pl.telech.tmoney.adm.controller.domain;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.adm.logic.SettingLogic;
import pl.telech.tmoney.adm.mapper.SettingMapper;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingController extends AbstractDomainController<Setting, SettingDto> {

	final SettingMapper mapper;
	final SettingLogic settingLogic;
	
	/*
	 * Returns current user.
	 */
	@RequestMapping(value = "", method = GET)
	public List<SettingDto> getAll() {
		
		return mapper.toDtoList(settingLogic.loadAll());
	}
}

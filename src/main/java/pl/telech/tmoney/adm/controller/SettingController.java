package pl.telech.tmoney.adm.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.telech.tmoney.adm.logic.SettingLogic;
import pl.telech.tmoney.adm.mapper.SettingMapper;
import pl.telech.tmoney.adm.model.dto.SettingDto;
import pl.telech.tmoney.commons.controller.AbstractController;

@RestController
@RequestMapping("/settings")
public class SettingController extends AbstractController {

	@Autowired
	SettingMapper mapper;
	
	@Autowired
	SettingLogic settingLogic;
	
	/*
	 * Returns current user.
	 */
	@RequestMapping(value = "", method = GET)
	public List<SettingDto> getAll() {
		
		return mapper.toDtoList(settingLogic.loadAll());
	}
}

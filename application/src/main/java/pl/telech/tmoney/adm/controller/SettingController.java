package pl.telech.tmoney.adm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.telech.processor.annotation.AutoMethod;
import pl.telech.processor.annotation.enums.Type;
import pl.telech.tmoney.commons.controller.AbstractController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingController extends AbstractController {

	
	@AutoMethod(type = Type.GET_ALL)
	private void init() {}
	
}

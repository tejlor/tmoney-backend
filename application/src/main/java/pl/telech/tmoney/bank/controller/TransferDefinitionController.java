package pl.telech.tmoney.bank.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.telech.processor.annotation.AutoMethod;
import pl.telech.processor.annotation.enums.Type;
import pl.telech.tmoney.commons.controller.AbstractController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer-definitions")
public class TransferDefinitionController extends AbstractController {
	
	@AutoMethod(type = Type.GET_BY_ID)
	@AutoMethod(type = Type.GET_TABLE)
	@AutoMethod(type = Type.CREATE)
	@AutoMethod(type = Type.UPDATE)
	@AutoMethod(type = Type.DELETE)
	void init() {}
	
}

package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.logic.BankLogic;
import pl.telech.tmoney.bank.model.data.TransferRequest;
import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfers")
public class TransferController {

	final BankLogic bankLogic;
	
	
	/*
	 * Creates transfer.
	 */
	@RequestMapping(value = "", method = POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void createTransfer(
			@RequestBody @Valid TransferRequest transfer) {
		
		bankLogic.createTransfer(transfer);
	}
	
}

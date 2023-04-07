package pl.telech.tmoney.bank.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.telech.tmoney.bank.logic.pdf.ReportService;
import pl.telech.tmoney.commons.controller.AbstractController;
import pl.telech.tmoney.commons.model.shared.FileResult;

@RestController
@RequestMapping("/reports")
public class ReportController extends AbstractController {

	@Autowired
	ReportService reportService;
	
	/*
	 * Generates table report.
	 */
	@RequestMapping(value = {"/table", "/table/{accountCode:" + CODE + "}"}, method = GET)
	public ResponseEntity<byte[]> generateTable(
			@PathVariable(required = false) String accountCode) {
		
		FileResult file = reportService.generateTable(accountCode);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getType())
				.body(file.getContent());
	}
	
	/*
	 * Generates year report.
	 */
	@RequestMapping(value = "/report", method = GET)
	public ResponseEntity<byte[]> generateReport(
			@RequestParam LocalDate dateFrom,
			@RequestParam LocalDate dateTo) {
		
		FileResult file = reportService.generateReport(dateFrom, dateTo);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getType())
				.body(file.getContent());
	}

}

package pl.telech.tmoney.bank.logic.report;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PROTECTED)
class AbstractPdfGenerator {

	final BaseFont baseFont;
	
	@SneakyThrows({DocumentException.class, IOException.class})
	protected AbstractPdfGenerator() {
		baseFont = BaseFont.createFont("fonts/tahoma.ttf", BaseFont.CP1250, true);
	}
}

package pl.telech.tmoney.bank.logic.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PROTECTED)
public class AbstractPdfGenerator {

	BaseFont baseFont;
	
	@SneakyThrows({DocumentException.class, IOException.class})
	protected void init() {
		baseFont = BaseFont.createFont("fonts/tahoma.ttf", BaseFont.CP1250, true);
	}
}

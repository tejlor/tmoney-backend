package pl.telech.tmoney.bank.logic.pdf;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PROTECTED)
public class AbstractChartGenerator {
	
	@SneakyThrows({FontFormatException.class, IOException.class})
	protected void init() {
		InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/tahoma.ttf");
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, fontStream));
	}
}

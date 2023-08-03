package pl.telech.tmoney.bank.logic.report;

import org.springframework.stereotype.Component;

import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.pdf.PdfChunk;

@Component
public class NoSplitCharacter implements SplitCharacter {

	@Override
	public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
		return false;
	}
}

package pl.telech.tmoney.bank.logic.pdf;

import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.pdf.PdfChunk;

public class MySplitCharacter implements SplitCharacter {

	@Override
	public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
		return false;
	}
}

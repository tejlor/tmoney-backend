package pl.telech.tmoney.bank.logic.pdf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.utils.TUtils;

public class TableHeaderFooterEventHelper extends PdfPageEventHelper {

    Account account;
    String version;
    
    Font font8gray, font12B;
    Image logo;
    float centerPosition;
    String dateTime;
    

    public TableHeaderFooterEventHelper(Account account, String version) {
    	this.account = account;  
    	this.version = version;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document doc) {        
    	initFonts();
    	initLogo(doc);      
    	precalculate(doc);
    }
    
    private void initFonts() {
    	BaseFont bf;
 		try {
 			bf = BaseFont.createFont("fonts/tahoma.ttf", BaseFont.CP1250, true);
 		} 
 		catch (DocumentException | IOException e) {
 			throw new TMoneyException("Cannot create font", e);
 		}
        font8gray = new Font(bf, 8, Font.NORMAL, BaseColor.DARK_GRAY);
        font12B = new Font(bf, 12, Font.BOLD);
    }
    
    private void initLogo(Document doc) {   
    	if (account == null)
    		return;
    	
    	byte[] decodedString = Base64.getDecoder().decode(new String(account.getLogo().substring(23)).getBytes());
    	try {
			logo = Image.getInstance(decodedString);
		} 
    	catch (BadElementException | IOException e) {
    		throw new TMoneyException("Cannot load logo image", e);
		}
    	logo.scalePercent(50);
        logo.setAbsolutePosition(doc.right() - logo.getScaledWidth(), doc.top() + 10);
    }
    
    private void precalculate(Document doc) {
    	centerPosition = (doc.right() - doc.left()) / 2 + doc.leftMargin();
    	dateTime = TUtils.formatDateTime(LocalDateTime.now());
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document doc) {
        if(account != null)
        	createHeader(writer, doc);
        createFooter(writer, doc);
    }
    
    private void createHeader(PdfWriter writer, Document doc) {
    	addTitle(writer, doc);
        addLogo(doc);
    }
    
    private void createFooter(PdfWriter writer, Document doc) {
        addGeneraterdInText(writer, doc);
        addPageNo(writer, doc);
    }
    
    private void addTitle(PdfWriter writer, Document doc) {
    	ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                new Phrase(account.getName(), font12B),
                centerPosition, doc.top() + 20, 0);      
    }
    
    private void addLogo(Document doc) {
		try {
			doc.add(logo);
		} 
		catch (DocumentException e) {
			throw new TMoneyException("Cannot add logo image", e);
		}
    }
    
    private void addGeneraterdInText(PdfWriter writer, Document doc) {
    	ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, 
            new Phrase(String.format("Wygenerowano w programie TMoney %s (%s)", version, dateTime), font8gray), 
            doc.leftMargin(), doc.bottom(), 0);
    }
    
    private void addPageNo(PdfWriter writer, Document doc) {
    	 ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, 
             new Phrase(String.format("str. %d", doc.getPageNumber()), font8gray), 
             doc.right(), doc.bottom(), 0);
    }
}

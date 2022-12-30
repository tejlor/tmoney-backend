package pl.telech.tmoney.bank.logic.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.FileResult;
import pl.telech.tmoney.commons.utils.TUtils;

@Component
public class PdfTableGenerator extends AbstractPdfGenerator {
	
    @Autowired
    ResourceLoader loader;
	
	@Value("${tmoney.version}")
	String tmoneyVersion;
	
    Font font10, font10B, font10red, font10green;
    
    
    @PostConstruct
	protected void init() {
    	super.init();
        font10 = new Font(baseFont, 10);
        font10B = new Font(baseFont, 10, Font.BOLD);
        font10red = new Font(baseFont, 10, Font.NORMAL, new BaseColor(200, 0, 0));
        font10green = new Font(baseFont, 10, Font.NORMAL, new BaseColor(0, 100, 0));
    }

    public FileResult generateFile(Account account, List<Entry> entries) {
        String fileName = String.format("%s.pdf", account.getName());

        byte[] content;
        try {
			content = createFile(account, entries);
		} 
        catch (DocumentException | IOException e) {
			throw new TMoneyException("Error during pdf file generation", e);
		}
        
        return new FileResult(fileName, MediaType.APPLICATION_PDF_VALUE, content);
    }
    
    private byte[] createFile(Account account, List<Entry> entries) throws DocumentException, IOException {   	
        var doc = new Document(PageSize.A4);
        doc.setMargins(30, 30, 70, 50);
        
    	var out = new ByteArrayOutputStream();
        var writer = PdfWriter.getInstance(doc, out);
        
        var logo = loader.getResource(String.format("classpath:images/%s.jpg", account.getCode().toLowerCase()));
        writer.setPageEvent(new TableHeaderFooterEventHelper(account, tmoneyVersion, logo.getURL()));
        doc.open();
        
        createTable(doc, entries, account.getCode().equals(Account.SUMMARY_CODE));
        doc.close();
        
        return out.toByteArray();
    }

    private void createTable(Document doc, List<Entry> entries, boolean summary) throws DocumentException {
        var pdfTable = new PdfPTable(5);
        pdfTable.setWidthPercentage(100);
        pdfTable.setWidths(new int[] { 1, 2, 6, 2, 2 });
        pdfTable.setHeaderRows(1);

        pdfTable.addCell(createHeaderCell("Id", Element.ALIGN_CENTER));
        pdfTable.addCell(createHeaderCell("Data", Element.ALIGN_CENTER));
        pdfTable.addCell(createHeaderCell("Nazwa", Element.ALIGN_LEFT));
        pdfTable.addCell(createHeaderCell("Kwota", Element.ALIGN_RIGHT));
        pdfTable.addCell(createHeaderCell("Stan", Element.ALIGN_RIGHT));  
        
        int row = 1;
        for(Entry entry : entries) {
        	BaseColor color = calculateBackgroundColor(summary, entry);        	
        	pdfTable.addCell(createIdBodyCell(entry, row, color));
        	pdfTable.addCell(createDateBodyCell(entry, row, color));
        	pdfTable.addCell(createNameBodyCell(entry, row, color));
        	pdfTable.addCell(createAmountBodyCell(entry, row, color));
        	pdfTable.addCell(createBalanceBodyCell(entry, row, summary, color));
        	row++;
        }
        doc.add(pdfTable);
    }
    
    private BaseColor calculateBackgroundColor(boolean summary, Entry entry) {
    	if (!summary) {
    		return null;
    	}
    	
    	return new BaseColor(entry.getAccount().getLightColorInt());
    }
    
    private PdfPCell createHeaderCell(String name, int align) {
        var cell = new PdfPCell(new Phrase(name, font10B));
        cell.setHorizontalAlignment(align);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setPaddingBottom(5);
        return cell;
    }
    
    private PdfPCell createIdBodyCell(Entry entry, int rowNo, BaseColor color) {
    	String value = entry.getId().toString();
    	return createBodyCell(value, font10, Element.ALIGN_CENTER, rowNo, color);
    }
   
    private PdfPCell createDateBodyCell(Entry entry, int rowNo, BaseColor color) {
    	String value = entry.getDate().toString();
    	return createBodyCell(value, font10, Element.ALIGN_CENTER, rowNo, color);
    }
    
    private PdfPCell createNameBodyCell(Entry entry, int rowNo, BaseColor color) {
    	String value = entry.getName();
    	return createBodyCell(value, font10, Element.ALIGN_LEFT, rowNo, color);
    }
    
    private PdfPCell createAmountBodyCell(Entry entry, int rowNo, BaseColor color) {
    	String value = TUtils.formatDecimal(entry.getAmount());
    	if(entry.getAmount().signum() > 0) {
    		value = "+" + value;
    	}
    	Font font = entry.getAmount().signum() > 0 ? font10green : font10red;
    	
    	return createBodyCell(value, font, Element.ALIGN_RIGHT, rowNo, color);
    }
    
    private PdfPCell createBalanceBodyCell(Entry entry, int rowNo, boolean summary, BaseColor color) {
    	String value = TUtils.formatDecimal(summary ? entry.getBalanceOverall() : entry.getBalance());    	
    	return createBodyCell(value, font10, Element.ALIGN_RIGHT, rowNo, color);
    }
    
    private PdfPCell createBodyCell(String value, Font font, int align, int row, BaseColor color) {
    	var cell = new PdfPCell(new Phrase(value, font));
        cell.setHorizontalAlignment(align);
        if (row % 50 == 0) {
            cell.setBorder(Rectangle.BOTTOM);
        }
        else {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        if(color != null) {
        	cell.setBackgroundColor(color);
        }
        return cell;
    }

}

package pl.telech.tmoney.bank.logic.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.SneakyThrows;
import pl.telech.tmoney.bank.dao.data.CategoryAmount;
import pl.telech.tmoney.bank.model.data.AccountReportData;
import pl.telech.tmoney.bank.model.data.ChartData;
import pl.telech.tmoney.bank.model.data.ReportData;
import pl.telech.tmoney.bank.model.data.SummaryReportData;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.FileResult;
import pl.telech.tmoney.commons.utils.TUtils;

@Component
class PdfReportGenerator extends AbstractPdfGenerator {
	   
    final NoSplitCharacter splitCharacter;
    final BarLineChartGenerator chartGenerator;
	final String tmoneyVersion;
	
    final Font font9, font10, font10B, font12, font16B;
    final BaseColor colorGray;
    final PdfPCell[] headerCells;
    
	protected PdfReportGenerator(NoSplitCharacter splitCharacter, BarLineChartGenerator chartGenerator, @Value("${tmoney.version}") String tmoneyVersion) {
		this.splitCharacter = splitCharacter;
		this.chartGenerator = chartGenerator;
		this.tmoneyVersion = tmoneyVersion;
		
    	font9 = new Font(baseFont, 9);
        font10 = new Font(baseFont, 10);
        font10B = new Font(baseFont, 10, Font.BOLD);
        font12 = new Font(baseFont, 12, Font.NORMAL);
        font16B = new Font(baseFont, 16, Font.BOLD);
        colorGray = new BaseColor(200, 200, 200);
        
        headerCells = new PdfPCell[5];
        headerCells[0] = createHeaderCell("Stan początkowy");
        headerCells[1] = createHeaderCell("Stan końcowy");
        headerCells[2] = createHeaderCell("Dochody");
        headerCells[3] = createHeaderCell("Straty");
        headerCells[4] = createHeaderCell("Bilans");
    }

    public FileResult generateFile(ReportData data) {
        String fileName = String.format("Raport %d.pdf", data.getDateFrom().getYear());

        byte[] content;
        try {
			content = createFile(data);
		} 
        catch (DocumentException | IOException e) {
			throw new TMoneyException("Error during pdf file generation", e);
		}
        
        return new FileResult(fileName, MediaType.APPLICATION_PDF_VALUE, content);
    }
    
    private byte[] createFile(ReportData data) throws DocumentException, IOException {   	
        var doc = new Document(PageSize.A4);
        doc.setMargins(30, 30, 20, 30);
        
    	var out = new ByteArrayOutputStream();
        var writer = PdfWriter.getInstance(doc, out);
        writer.setPageEvent(new TableHeaderFooterEventHelper(null, tmoneyVersion));
        doc.open();
        
        createReport(doc, data);
        doc.close();
        
        return out.toByteArray();
    }

    private void createReport(Document doc, ReportData data) throws DocumentException {
        var p = new Paragraph("RAPORT " + data.getDateFrom().getYear(), font16B);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);

        var pdfTable = new PdfPTable(5);
        pdfTable.setWidthPercentage(100);
        pdfTable.setSpacingBefore(20);
        pdfTable.setWidths(new int[] { 1, 1, 1, 1, 1 });
        
        for(var account : data.getAccountsData()) {
        	createAccountData(pdfTable, account);
        }
        createSummaryData(pdfTable, data.getSummaryData());
        
        doc.add(pdfTable);

        createList(doc, "Przychody:", data.getSummaryData().getIncomes());
        createList(doc, "Straty:", data.getSummaryData().getOutcomes());
        createList(doc, "Bilans:", data.getSummaryData().getProfits());

        createChart(doc, data.getChartData());
    }

    private void createAccountData(PdfPTable pdfTable, AccountReportData data) {         	
    	pdfTable.addCell(createAccountCell(data.getAccount().getName()));
        pdfTable.addCell(headerCells[0]);
        pdfTable.addCell(headerCells[1]);
        pdfTable.addCell(headerCells[2]);
        pdfTable.addCell(headerCells[3]);
        pdfTable.addCell(headerCells[4]);

        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getInitialBalance())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getFinalBalance())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getIncomesSum())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getOutcomesSum())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.calcBalance())));
    }
    
    private void createSummaryData(PdfPTable pdfTable, SummaryReportData data) {         	
    	pdfTable.addCell(createAccountCell("Podsumowanie"));
        pdfTable.addCell(headerCells[0]);
        pdfTable.addCell(headerCells[1]);
        pdfTable.addCell(headerCells[2]);
        pdfTable.addCell(headerCells[3]);
        pdfTable.addCell(headerCells[4]);

        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getInitialBalance())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getFinalBalance())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getIncomesSum())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getOutcomesSum())));
        pdfTable.addCell(createValueCell(TUtils.formatCurrency(data.getProfitsSum())));
    }

    private void createList(Document doc, String title, List<CategoryAmount> data) throws DocumentException {
        var titleP = new Paragraph(title, font10B);
        titleP.setSpacingBefore(8);	
    	doc.add(titleP);

        var p = new Paragraph();
        p.setSpacingBefore(0);
        p.setFont(font9);

        for(int i = 0, size = data.size(); i < size; i++) {
        	CategoryAmount in = data.get(i);
            String separator = (i != size - 1 ? ", " : ".");
            var ch = new Chunk(String.format("%s (%s)%s", in.getCategoryName(), TUtils.formatCurrency(in.getAmount()), separator));
            ch.setSplitCharacter(splitCharacter);
            p.add(ch);
        }
        doc.add(p);
    }

    private PdfPCell createHeaderCell(String title) {
        var cell = new PdfPCell(new Phrase(title, font10B));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.BOTTOM);
        return cell;
    }
    
    private PdfPCell createValueCell(String title) {
        var cell = new PdfPCell(new Phrase(title, font10));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(10);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell createAccountCell(String str) {
        var cell = new PdfPCell(new Phrase(str, font12));
        cell.setColspan(5);
        cell.setBackgroundColor(colorGray);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(1);
        cell.setPaddingBottom(4);
        return cell;
    }
    
    @SneakyThrows({BadElementException.class, IOException.class})
    private void createChart(Document doc, ChartData data) throws DocumentException {
     	doc.add(new Paragraph(" ", font10)); // margin

    	byte[] chartBytes = chartGenerator.renderChart(data);
    	var image = Image.getInstance(chartBytes);
    	image.scaleAbsolute(530, 300);
    	image.setAlignment(Element.ALIGN_CENTER);
    	image.setSpacingBefore(10);
    	doc.add(image);
    }

}

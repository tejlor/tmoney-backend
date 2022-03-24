package pl.telech.tmoney.commons.logic.helper;

import static lombok.AccessLevel.PRIVATE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.exception.TMoneyException;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class ExcelExporter<T> {

	String title;
	List<Column<T>> columns = new ArrayList<>();
	List<T> dataSet;
	
	public ExcelExporter<T> column(Column<T> column) {
		column.index = columns.size();
		columns.add(column);
		return this;
	}
	
	public ByteArrayOutputStream create() {
		var out = new ByteArrayOutputStream();
		try {
			createWorkbook().write(out);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new TMoneyException(e.getMessage());
		}
		return out;
	}
	
	private XSSFWorkbook createWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("List");
		
		CellStyle titleStyle = titleStyle(workbook);
		CellStyle timeStyle = timeStyle(workbook);
		CellStyle headerStyle = headerStyle(workbook);
		
		renderTitle(sheet, titleStyle, timeStyle);
		renderTableHeader(sheet, headerStyle);
		renderTableData(sheet);
		
		for(Column<T> column : columns) {
			sheet.autoSizeColumn(column.index);
		}
		
		return workbook;
	}
	
	private void renderTitle(XSSFSheet sheet, CellStyle titleStyle, CellStyle timeStyle) {
		XSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints((1.5f * sheet.getDefaultRowHeightInPoints()));
		XSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(new XSSFRichTextString(title));
		titleCell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size()));
		
		XSSFRow timeRow = sheet.createRow(1);
		XSSFCell timeCell = timeRow.createCell(0);
		timeCell.setCellValue(new XSSFRichTextString(LocalDateTime.now().toString()));
		timeCell.setCellStyle(timeStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, columns.size()));
	}
	
	private void renderTableHeader(XSSFSheet sheet, CellStyle headerStyle) {
		XSSFRow headerRow = sheet.createRow(3);
		headerRow.setHeightInPoints((1.5f * sheet.getDefaultRowHeightInPoints()));

		for(Column<T> col : columns) {
			XSSFCell headerCell = headerRow.createCell(col.index);
			headerCell.setCellValue(new XSSFRichTextString(col.title));
			headerCell.setCellStyle(headerStyle);
		}
	}
	
	private void renderTableData(XSSFSheet sheet) {
		int currentRow = 4;
		for (T rowData : dataSet) {
			XSSFRow row = sheet.createRow(currentRow);		
			for(Column<T> col : columns) {				
				XSSFCell cell = row.createCell(col.index);
				cell.setCellValue(new XSSFRichTextString(col.getter.apply(rowData).toString()));
			}
			currentRow++;
		}
	}
	
	private CellStyle titleStyle(XSSFWorkbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(blackBoldFont(workbook));
		return headerStyle;
	}
	
	private CellStyle timeStyle(XSSFWorkbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(italicFont(workbook));
		return headerStyle;
	}
	
	private CellStyle headerStyle(XSSFWorkbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(HSSFColorPredefined.ROYAL_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(whiteBoldFont(workbook));
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return headerStyle;
	}
	
	private Font whiteBoldFont(XSSFWorkbook workbook) {
		var font = workbook.createFont();
		font.setBold(true);
		font.setColor(HSSFColorPredefined.WHITE.getIndex());
		return font;
	}
	
	private Font blackBoldFont(XSSFWorkbook workbook) {
		var font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(14);
		return font;
	}
	
	private Font italicFont(XSSFWorkbook workbook) {
		var font = workbook.createFont();
		font.setItalic(true);
		return font;
	}
	
	@Getter
	@Accessors(chain = false, fluent = false)
	public static class Column<T> {
		int index;
		String title;
		Function<T, ?> getter;
		
		public Column(String title, Function<T,?> getter) {
			this.title = title;
			this.getter = getter;
		}
	}
}

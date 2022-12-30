package pl.telech.tmoney.bank.logic.pdf;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import pl.telech.tmoney.bank.model.data.ChartData;
import pl.telech.tmoney.bank.model.data.MonthData;

@Component
public class BarLineChartGenerator extends AbstractChartGenerator {

	Font monthFont, barFont, lineFont;
	Color colorGreen, colorRed, colorBlue;
	DecimalFormat decShortFormat, decLongFormat, decCurrFormat;
	CategoryItemLabelGenerator shortLabelGenerator, longLabelGenerator;
	

	@PostConstruct
	public void init() {
		super.init();

		monthFont = new Font("Tahoma", Font.PLAIN, 16);
		barFont = new Font("Tahoma", Font.BOLD, 10);
		lineFont = new Font("Tahoma", Font.BOLD, 14);

		colorGreen = new Color(0, 150, 0);
		colorRed = new Color(200, 0, 0);
		colorBlue = new Color(0, 0, 255);
		
		decShortFormat = new DecimalFormat("0");
		decLongFormat = new DecimalFormat("##,##0");
		decCurrFormat = new DecimalFormat("##,##0 zł");
		
		shortLabelGenerator = new StandardCategoryItemLabelGenerator("{2}", decShortFormat);
		longLabelGenerator = new StandardCategoryItemLabelGenerator("{2}", decLongFormat);
	}

	@SneakyThrows(IOException.class)
	public byte[] renderChart(ChartData data) {
		var movementsDataSet = new DefaultCategoryDataset();
		var balanceDataSet = new DefaultCategoryDataset();

		for (MonthData month : data.getMonths()) {
			String monthName = month.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());

			movementsDataSet.addValue(month.getIncome(), "Dochód", monthName);
			movementsDataSet.addValue(month.calcProfit(), "Zysk", monthName);
			movementsDataSet.addValue(month.getOutcome(), "Strata", monthName);
			balanceDataSet.addValue(month.getBalance(), "Bilans", monthName);
		}

		var chart = new JFreeChart(createPlot(movementsDataSet, balanceDataSet));
		chart.setBackgroundPaint(Color.white);
		chart.setPadding(new RectangleInsets(10, 0, 0, 0));
		chart.getLegend().setVisible(false);

		var out = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsPNG(out, chart, 1060, 600);
		return out.toByteArray();
	}

	private CategoryPlot createPlot(DefaultCategoryDataset movementsDataSet, DefaultCategoryDataset balanceDataSet) {
		var plot = new CategoryPlot();

		plot.setDataset(0, movementsDataSet);
		plot.setDataset(1, balanceDataSet);
		
		plot.setRenderer(0, createBarRenderer());
		plot.setRenderer(1, createLineRenderer());

		plot.setDomainAxis(createCategoryAxis());
		plot.setRangeAxis(0, createLeftAxis());
		plot.setRangeAxis(1, createRightAxis());

		plot.mapDatasetToRangeAxis(0, 0);
		plot.mapDatasetToRangeAxis(1, 1);

		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);

		return plot;
	}
	
	private BarRenderer createBarRenderer() {
		var renderer = new BarRenderer();
		addSeries(renderer, 0, colorGreen);
		addSeries(renderer, 1, colorBlue);
		addSeries(renderer, 2, colorRed);
		return renderer;
	}
	
	private LineAndShapeRenderer createLineRenderer() {
		var renderer = new LineAndShapeRenderer();
		addSeries(renderer, 0, colorBlue);
		return renderer;
	}
	
	private void addSeries(BarRenderer renderer, int series, Color color) {
		renderer.setSeriesPaint(series, color);
		renderer.setSeriesItemLabelPaint(series, color);
		renderer.setSeriesItemLabelFont(series, barFont);
		renderer.setSeriesItemLabelGenerator(series, shortLabelGenerator);
		renderer.setSeriesItemLabelsVisible(series, true);
	}
	
	private void addSeries(LineAndShapeRenderer renderer, int series, Color color) {
		renderer.setSeriesPaint(series, color);
		renderer.setSeriesShape(series, new Ellipse2D.Double(-4, -4, 8, 8));
		renderer.setSeriesItemLabelFont(series, lineFont);
		renderer.setSeriesItemLabelGenerator(series, longLabelGenerator);
		renderer.setSeriesItemLabelsVisible(series, true);
	}
	
	private CategoryAxis createCategoryAxis() {
		var axis = new CategoryAxis();
		axis.setTickLabelFont(monthFont);
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		return axis;
	}
	
	private NumberAxis createLeftAxis() {
		var axis = new NumberAxis();
		axis.setTickLabelFont(monthFont);
		axis.setNumberFormatOverride(decCurrFormat);
		axis.setRange(-5000, 65_000);
		return axis;
	}
	
	private NumberAxis createRightAxis() {
		var axis = new NumberAxis();
		axis.setTickLabelFont(monthFont);
		axis.setTickLabelPaint(colorBlue);
		axis.setNumberFormatOverride(decCurrFormat);
		axis.setRange(0, 600_000);
		return axis;
	}
}

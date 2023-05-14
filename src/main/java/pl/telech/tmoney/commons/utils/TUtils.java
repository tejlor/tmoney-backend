package pl.telech.tmoney.commons.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.xml.bind.DatatypeConverter;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.exception.NotFoundException;
import pl.telech.tmoney.commons.model.exception.TMoneyException;

@Slf4j
@UtilityClass
public class TUtils {
	
	private static final DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
	private static final DecimalFormat currencyFormat = new DecimalFormat("#,###,###,##0.00 zł");
	private static final DecimalFormat percentFormat = new DecimalFormat("#,###,###,##0.00 %");
	
	private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static final DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");
	public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
		
	/* ################################################# String #################################################################### */

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		
		return (str.trim().length() == 0);
	}
	
	public static boolean isEqual(String a, String b) {
		if(a == null)
			a = "";
		if(b == null)
			b = "";
		
		return a.equals(b);
	}
	
	public static String encodeToBase64(byte[] file) {
		if (file == null) 
			return null;
		
		return DatatypeConverter.printBase64Binary(file);
	}

	public static byte[] decodeFromBase64(String base64) {
		return DatatypeConverter.parseBase64Binary(base64);
	}
	
	/* ################################################# Decimal #################################################################### */

	public static boolean isZero(BigDecimal val){
		if (val == null)
			return true;
		else
			return (val.compareTo(BigDecimal.ZERO) == 0);
	}
	
	public static String formatDecimal(BigDecimal val){
		return val != null ? decimalFormat.format(val) : null;
	}
	
	public static String formatDecimal(double val){
		return decimalFormat.format(val);
	}
	
	public static String formatCurrency(BigDecimal val){
		return val != null ? currencyFormat.format(val) : null;
	}
	
	public static String formatCurrency(double val){
		return currencyFormat.format(val);
	}
	
	public static String formatPercent(BigDecimal val){
		return val != null ? percentFormat.format(val) : null;
	}
	
	public static String formatPercent(double val){
		return percentFormat.format(val);
	}
	
	public static void fillZeros(BigDecimal[] array){
		for(int i = 0; i < array.length; i++)
			array[i] = BigDecimal.ZERO;
	}
	
	public static <T> BigDecimal sum(List<T> list, Function<T, BigDecimal> mapper){
		return list.stream()
				.map(mapper)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public static <T> BigDecimal sum(List<T> list, Function<T, BigDecimal> mapper, Predicate<T> filter){
		return list.stream()
				.filter(filter)
				.map(mapper)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/* ################################################# Date #################################################################### */
		
	public static LocalDate startOfYear(int year){
		return LocalDate.of(year,  1, 1);
	}
	
	public static LocalDate endOfYear(int year){
		return LocalDate.of(year,  12, 31);
	}
	
	public static LocalDate startOfMonth(LocalDate date){
		return date.withDayOfMonth(1);
	}
	
	public static LocalDate endOfMonth(LocalDate date){
		return date.withDayOfMonth(date.lengthOfMonth());
	}
	
	public static String formatDateTime(LocalDateTime dateTime) {
		return dateTime.format(dateTimeFormat);
	}
	
	public static void assertMonthDate(LocalDate date) {
		if(date.getDayOfMonth() != 1)
			throw new TMoneyException("Incorrect month date.");
	}
	
	public static void assertYear(int year) {
		if(year < 2010 || year > 2050)
			throw new TMoneyException("Incorrect year.");
	}
	
	public static void assertStartBeforeEnd(LocalDate start, LocalDate end) {
		if(start.isAfter(end))
			throw new TMoneyException("Incorrect period.");
	}
	
	/* ################################################# Encoding #################################################################### */
	
	public static String md5(String str){
        MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return "";
		}
        m.reset();
        m.update(str.getBytes());

        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashText = bigInt.toString(16);
        
        while(hashText.length() < 32 ){
        	hashText = "0" + hashText;
        }

        return hashText;
	}
	
	public static String sha1(String str) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("SHA-1");
		}
		catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return "";
		}
        m.reset();
        m.update(str.getBytes());

        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashText = bigInt.toString(16);

        return hashText;
	}
	
	/* ################################################# Environment #################################################################### */
	
	public static boolean isDev(String environment){
		return environment.equals("DEV");
	}
	
	public static boolean isProd(String environment){
		return environment.equals("PROD");
	}
	
	public static boolean isJUnit(String environment){
		return environment.equals("JUNIT");
	}

	/*
	 * Checks if object id in body is the same as id in url (update methods - PUT).
	 */
	public static void assertDtoId(long id, AbstractDto dto) {
		if(dto.getId() != null && id != dto.getId())
			throw new TMoneyException("Object id is diferent than value of request parameter.");
	}
	
	public static void assertEntityExists(AbstractEntity model) {
		if(model == null)
			throw new TMoneyException("Object with given id does not exists.");
	}
	
	public static void assertResultExists(Object result) {
		if(result == null)
			throw new NotFoundException();
	}
	
}

package pl.telech.tmoney.commons.model.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TMoneyException extends RuntimeException {
	
	public TMoneyException(){
		super();
	}
	
	public TMoneyException(String msg){
		super(msg);
	}
	
	public TMoneyException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	public String toShortString(){
		return Arrays.stream(getStackTrace())
			.limit(5)
			.map(st -> st.toString())
			.collect(Collectors.joining("\n  ", getMessage() + "\n  ", "\n  ..."));
	}
}

package pl.telech.tmoney.commons.config.async;

import java.lang.reflect.Method;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/*
 * Exception handler for exceptions throwed from async methods.
 */
@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		var sb = new StringBuilder("Async method: " + method.getName() + "\n");
		for (Object param : obj) {
			sb.append("Parameter value = " + param + "\n");
		}
		sb.append(ExceptionUtils.getStackTrace(throwable));
		log.error(sb.toString());
	}
}

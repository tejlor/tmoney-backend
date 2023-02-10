package pl.telech.tmoney.commons.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;

/*
 * Filter adding wrapper for request, which allows to read content many times. Required for logging.
 */
@Component
public class CachingRequestBodyFilter extends GenericFilterBean {
    
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
        var currentRequest = (HttpServletRequest) request;
        var wrappedRequest = new ContentCachingRequestWrapper(currentRequest);
        wrappedRequest.getParameterMap(); // needed for caching!!
        chain.doFilter(wrappedRequest, response);
    }	
}

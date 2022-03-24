package pl.telech.tmoney.commons.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/*
 * Filter adding wrapper for request, which allows to read content twice. Required for logging.
 */
@Component
public class CachingRequestBodyFilter extends GenericFilterBean {
    
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {		
        var currentRequest = (HttpServletRequest) servletRequest;
        var wrappedRequest = new RequestWrapper(currentRequest);
        chain.doFilter(wrappedRequest, servletResponse);
    }	
}

package pl.telech.tmoney.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import pl.telech.tmoney.adm.logic.UserLogic;
import pl.telech.tmoney.commons.utils.Sha1PasswordEncoder;

/*
 * Configuration of oAuth 2.0.
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${tmoney.environment}")
	String environment;
	
	@Autowired
	UserLogic userLogic;
	
	@Bean
	public AuthenticationProvider authProvider() {
	    var authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService((UserDetailsService) userLogic);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.authenticationProvider(authProvider());
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/oauth/token").permitAll()
			.antMatchers("/v2/api-docs/**", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
			.anyRequest().authenticated();
    }	

	@Bean
	@ConditionalOnProperty(name = "tmoney.environment", havingValue = "DEV")
	public FilterRegistrationBean<CorsFilter> corsFilter() {	
		var config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("PATCH");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("OPTIONS");
		config.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
    public static PasswordEncoder passwordEncoder() {
    	return new Sha1PasswordEncoder();
    }
	
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
}

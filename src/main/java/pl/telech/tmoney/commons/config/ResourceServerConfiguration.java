package pl.telech.tmoney.commons.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/*
 * Configuration of oAuth 2.0.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("tmoney").stateless(true);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/v2/api-docs/**", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}

package pl.telech.tmoney.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import pl.telech.tmoney.adm.logic.UserLogic;

/*
 * Configuration of oAuth 2.0.
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${tmoney.auth.clientName}")
	String clientName;
	
	@Value("${tmoney.auth.clientPass}")
	String clientPass;
	
	@Value("${tmoney.auth.accessTokenExpTime}")
	int accessTokenExpTime;
	
	@Value("${tmoney.auth.refreshTokenExpTime}")
	int refreshTokenExpTime;

	@Autowired
	@Qualifier("authenticationManagerBean")
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserLogic userLogic;
	
	@Autowired
	TokenStore tokenStore;

	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient(clientName)
			.authorizedGrantTypes("password", "refresh_token")
			.scopes("read")
			.secret(clientPass)
			.accessTokenValiditySeconds(accessTokenExpTime) 		
			.refreshTokenValiditySeconds(refreshTokenExpTime); 
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore)
			.authenticationManager(authenticationManager).userDetailsService((UserDetailsService) userLogic);
	}
}

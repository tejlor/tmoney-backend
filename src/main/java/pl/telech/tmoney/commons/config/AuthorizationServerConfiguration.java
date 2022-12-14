package pl.telech.tmoney.commons.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import pl.telech.tmoney.adm.logic.interfaces.UserLogic;

/*
 * Configuration of oAuth 2.0.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

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
    DataSource dataSource;
	
	@Autowired
	UserLogic userLogic;

    @Bean
    public TokenStore tokenStore() {
        var tokenStore = new JdbcTokenStore(dataSource);
        tokenStore.setInsertAccessTokenSql("INSERT INTO auth.access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) VALUES (?, ?, ?, ?, ?, ?, ?)");
        tokenStore.setSelectAccessTokenSql("SELECT token_id, token FROM auth.access_token WHERE token_id = ?");
        tokenStore.setSelectAccessTokenAuthenticationSql("SELECT token_id, authentication FROM auth.access_token WHERE token_id = ?");
        tokenStore.setSelectAccessTokenFromAuthenticationSql("SELECT token_id, token FROM auth.access_token WHERE authentication_id = ?");
        tokenStore.setSelectAccessTokensFromUserNameAndClientIdSql("SELECT token_id, token FROM auth.access_token WHERE user_name = ? AND client_id = ?");
        tokenStore.setSelectAccessTokensFromUserNameSql("SELECT token_id, token FROM auth.access_token WHERE user_name = ?");
        tokenStore.setSelectAccessTokensFromClientIdSql("SELECT token_id, token FROM auth.access_token WHERE client_id = ?");
        tokenStore.setDeleteAccessTokenSql("DELETE FROM auth.access_token WHERE token_id = ?");
        tokenStore.setDeleteAccessTokenFromRefreshTokenSql("DELETE FROM auth.access_token WHERE refresh_token = ?");
        tokenStore.setInsertRefreshTokenSql("INSERT INTO auth.refresh_token (token_id, token, authentication) VALUES (?, ?, ?)");
        tokenStore.setSelectRefreshTokenSql("SELECT token_id, token FROM auth.refresh_token WHERE token_id = ?");
        tokenStore.setSelectRefreshTokenAuthenticationSql("SELECT token_id, authentication FROM auth.refresh_token WHERE token_id = ?");
        tokenStore.setDeleteRefreshTokenSql("DELETE FROM auth.refresh_token WHERE token_id = ?");      
        return tokenStore;
    }
	
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
			.tokenStore(tokenStore())
			.authenticationManager(authenticationManager).userDetailsService((UserDetailsService) userLogic);
	}
}

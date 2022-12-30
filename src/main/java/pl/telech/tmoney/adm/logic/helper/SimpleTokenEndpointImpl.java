package pl.telech.tmoney.adm.logic.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;

import pl.telech.tmoney.adm.logic.helper.interfaces.SimpleTokenEndpoint;
import pl.telech.tmoney.adm.logic.interfaces.UserLogic;
import pl.telech.tmoney.commons.model.exception.TMoneyException;

/*
 * Class simulate endpoint for token generating. It is used to generate token for user without knowing of his password.
 */
@Component
public class SimpleTokenEndpointImpl implements SimpleTokenEndpoint {
		
	@Autowired
	UserLogic userLogic;
	@Autowired
	AuthorizationServerTokenServices tokenServices;
	@Autowired
	ClientDetailsService clientDetailsService;
	

	public ResponseEntity<OAuth2AccessToken> postAccessToken(String clientId, Map<String, String> parameters) {
		ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
		TokenRequest tokenRequest = new TokenRequest(parameters, clientId, client.getScope(), "password");
		OAuth2Authentication authentication = getOAuth2Authentication(client, tokenRequest);
		OAuth2AccessToken token = tokenServices.createAccessToken(authentication);
		return getResponse(token);
	}
	
	private OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		Map<String, String> parameters = tokenRequest.getRequestParameters();
		String username = parameters.get("username");
		String password = parameters.get("password");

		UserDetails user = userLogic.loadUserByUsername(username);
		if(!user.isEnabled())
			throw new TMoneyException("Your account is inactive.");
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
		token.setDetails(parameters);
		
		OAuth2Request storedOAuth2Request = tokenRequest.createOAuth2Request(client);		
		return new OAuth2Authentication(storedOAuth2Request, token);
	}
	
	private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		headers.set("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
	}
}

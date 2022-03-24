package pl.telech.tmoney.adm.logic.helper.interfaces;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface SimpleTokenEndpoint {
	
	ResponseEntity<OAuth2AccessToken> postAccessToken(String clientId, Map<String, String> parameters);
}

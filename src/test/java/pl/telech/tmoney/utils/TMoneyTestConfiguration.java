package pl.telech.tmoney.utils;

import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@TestConfiguration
public class TMoneyTestConfiguration {
 
    /*@Bean
    @Primary
    public SimpleTokenEndpoint createSimpleTokenEndpoint(){
        return new SimpleTokenEndpoint() {
			
			@Override
			public ResponseEntity<OAuth2AccessToken> postAccessToken(String clientId, Map<String, String> parameters) {		
				HttpHeaders headers = new HttpHeaders();
				headers.set("Cache-Control", "no-store");
				headers.set("Pragma", "no-cache");
				headers.set("Content-Type", "application/json;charset=UTF-8");
				return new ResponseEntity<OAuth2AccessToken>(new DefaultOAuth2AccessToken("token"), headers, HttpStatus.OK);
			}
		};
    }*/
}

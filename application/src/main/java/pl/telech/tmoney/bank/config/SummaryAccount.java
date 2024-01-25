package pl.telech.tmoney.bank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "tmoney.summary-account")
public class SummaryAccount {
	
	String code;
	String name;						
	String color;
	String icon;
	String logo;
}

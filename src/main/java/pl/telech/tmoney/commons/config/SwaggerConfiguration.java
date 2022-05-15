package pl.telech.tmoney.commons.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile({"dev"})
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)  
		          .select()                                  
		          .apis(RequestHandlerSelectors.basePackage("pl.telech.tmoney"))                        
		          .build()
		          .apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "TMoney", 
	      "TMoney backend spring-boot application", 
	      "1.0", 
	      "Terms of service", 
	      new Contact("Krzysztof Telech", "www.telech.olawa.pl", "krzysztof@telech.olawa.pl"), 
	      "License of API", "API license URL", Collections.emptyList());
	}
	
}

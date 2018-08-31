package org.mes.myexpenses.commons.swagger.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


// http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "swagger.config")
@EnableSwagger2
//@Import(SpringDataRestConfiguration.class)
//@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerConfig {                                    
	private String title = "MyExpenses Spring Boot REST API";
	private String description = "\"Spring Boot REST API for MyExpenses application\"";
	private String version = "1.0.0";

	@PostConstruct
	public void init() {
		log.info("SwaggerConfig [title={}, description={}, version={}]", title, description, version);
	}
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))		// business services
          .paths(PathSelectors.any())
          .build()
          .apiInfo(metaData());
    }
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .build();
    }
}

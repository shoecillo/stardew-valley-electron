package com.sh.app;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan("com.sh")
@EnableSwagger2
@EnableMongoRepositories(basePackages="com.sh.repository")
@SpringBootApplication
public class Stardew 
{
	
	@Value("${mongo.host}")
	private String host;
	@Value("${mongo.port}")
	private String port;
	@Value("${mongo.db}")
	private String db;
	@Value("${mongo.coll}")
	private String coll;
	
	
	public static void main(String[] args)
	{
		SpringApplication.run(Stardew.class, args);
		
	}
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.sh.ctrl"))              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
    
	@Bean
	public MongoDbFactory mongoDbFactory()
	{
		return new SimpleMongoDbFactory(new MongoClient(host, Integer.parseInt(port)), db);
	}
	@Bean
	public MongoTemplate mongoTemplate()
	{
		MongoTemplate template = new MongoTemplate(mongoDbFactory());
		return template;
	}
	@Bean
	public DBCollection workingColl()
	{
		return mongoTemplate().getCollection(coll);
	}
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("OPTIONS");
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
}

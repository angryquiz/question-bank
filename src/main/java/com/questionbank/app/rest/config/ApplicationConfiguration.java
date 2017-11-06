package com.questionbank.app.rest.config;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
/**
 * Configuration bean to set up Swagger.
 */
@Component
public class ApplicationConfiguration {

	private String elasticSearchUrl = null;

	private static Logger LOG = Logger.getLogger(ApplicationConfiguration.class);
	
    @PostConstruct
    public void init() {
      
    	String elasticSearchUrlProp = System.getProperty("elasticSearchUrl");
    	
    	LOG.info("Initializing ElasticSearch URL");
		
    	if (StringUtils.isEmpty(elasticSearchUrlProp)) {
        	LOG.info("Using default URL...");
    		elasticSearchUrl = "http://localhost:9200";
		} 
    	
		LOG.info("elasticSearchUrl: " + elasticSearchUrlProp);
		
    	elasticSearchUrl = elasticSearchUrlProp;
    	
    }

	public String getElasticSearchUrl() {
		return elasticSearchUrl;
	}

	public void setElasticSearchUrl(String elasticSearchUrl) {
		this.elasticSearchUrl = elasticSearchUrl;
	}
    
    

}

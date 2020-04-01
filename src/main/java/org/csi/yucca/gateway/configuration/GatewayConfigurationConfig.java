/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewayConfigurationConfig {

	@Autowired
	private YuccaSettings yuccaSettings;
	
	@Bean
	@Autowired
	RestTemplate metadataRestTemplate() {
		return  new TestRestTemplate(yuccaSettings.getTenant().getUsername(), yuccaSettings.getTenant().getPassword());
	}
	
	
	@Bean
	@Autowired
	StreamConfigurationManager streamConfigurationManager() {
		return new StreamConfigurationManager(yuccaSettings.getMetadata().getHttpEndpoint(), yuccaSettings.getTenant().getCode());
	}

	@Bean
	@Autowired
	StreamConfigurationDAO streamConfigurationDAO(){
		return new StreamConfigurationDAO();
	}
	
	@Bean
	@Autowired
	ApplicationStartedEventHolderBean applicationStartedEventHolderBean(){
		return new ApplicationStartedEventHolderBean();
	}
	
	
}

/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartedEventHolderBean {
	private static final Logger log=Logger.getLogger("org.csi.yucca.datainsert");

	
	private Boolean eventFired = false;
	 
	@Autowired
	private StreamConfigurationManager streamConfigurationManager;
	
	@Autowired
	private YuccaSettings setting;
	
	
    public Boolean getEventFired() {
        return eventFired;
    }
 
    public void setEventFired(ContextRefreshedEvent applicationStartedEvent, Boolean eventFired) {
    	
    	String configurationErrors = setting.getConfigurationErrors(); 
    	
    	if (configurationErrors!=null)
    	{
    		throw new ApplicationContextException("You have configuration problem (maybe you don't have custom application.properties?)\n"+configurationErrors);
    	}
    		
    	else {
    		if (setting.getMetadata().getRefreshOnStartup())
    			streamConfigurationManager.refreshConfiguration();
    	}
        this.eventFired = eventFired;
    }
    
    
    @Scheduled(cron = "${yucca.metadata.refreshCron}" )
    public void demoServiceMethod()
    {
        streamConfigurationManager.refreshConfiguration();
    }
}

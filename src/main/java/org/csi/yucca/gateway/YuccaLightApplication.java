/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway;

import org.csi.yucca.gateway.configuration.ApplicationStartedEventHolderBean;
import org.csi.yucca.gateway.configuration.ApplicationStartedEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@Configuration
@PropertySources({@PropertySource(value={"classpath:internal.properties"}),@PropertySource(value={"${ext.application.properties}"})})
public class YuccaLightApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx=SpringApplication.run(YuccaLightApplication.class, args);
   		ApplicationStartedEventHolderBean eventHolderBean = ctx.getBean(ApplicationStartedEventHolderBean.class);
    }


    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<YuccaLightApplication> applicationClass = YuccaLightApplication.class;
	

}

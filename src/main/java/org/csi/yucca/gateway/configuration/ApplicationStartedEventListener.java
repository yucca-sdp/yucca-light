/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class ApplicationStartedEventListener implements ApplicationListener{

	private ApplicationStartedEventHolderBean eventHolderBean;

	@Autowired
	public void setEventHolderBean(ApplicationStartedEventHolderBean eventHolderBean) {
		this.eventHolderBean = eventHolderBean;
	}

	//    @Override
	//    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
	//        System.out.println("Context Event Received ----------------------------------------------------------------------");
	//        eventHolderBean.setEventFired(true);
	//    }	


	@Override
	public void onApplicationEvent(ApplicationEvent  applicationStartedEvent) {
		
		
		System.out.println("event +++++"  + applicationStartedEvent.toString());
		 if (applicationStartedEvent instanceof ContextRefreshedEvent) {
			this.eventHolderBean.setEventFired((ContextRefreshedEvent)applicationStartedEvent,true);
		 }
	}	

}

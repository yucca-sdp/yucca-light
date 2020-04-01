/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration.util;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;

public class IntegrationTestUtils {

	public static Integer countQueueElement(String queueName, JmsTemplate jmsTemplate) {
		Integer num = jmsTemplate.browse(queueName,new BrowserCallback<Integer>() {
			@Override
			public Integer doInJms(Session arg0, QueueBrowser queueBrowser) throws JMSException {
				    Enumeration e = (Enumeration) queueBrowser.getEnumeration();
	
				    int numMsgs = 0;
				    while(e.hasMoreElements()) 
				    {
				         e.nextElement();
				         numMsgs++;
				    }
				return numMsgs;
			}
			
		} );
		return num;
	}

	public static Integer logQueueLastElement(String queueName, JmsTemplate jmsTemplate) {
		Integer num = jmsTemplate.browse(queueName,new BrowserCallback<Integer>() {
			@Override
			public Integer doInJms(Session arg0, QueueBrowser queueBrowser) throws JMSException {
				    Enumeration e = (Enumeration) queueBrowser.getEnumeration();
	
				    int numMsgs = 0;
				    
				    while(e.hasMoreElements()) 
				    {
				    	ActiveMQObjectMessage o  =(ActiveMQObjectMessage)e.nextElement();
				         if (!e.hasMoreElements())
				         {
				        	 System.out.println("Last element:"+o.getObject().toString());
				         }
				    }
				return numMsgs;
			}
			
		} );
		return num;
	}
}

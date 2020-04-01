/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.log4j.Logger;
import org.csi.yucca.gateway.api.InputManager;
import org.csi.yucca.gateway.api.dto.ErrorOnInbound;
import org.csi.yucca.gateway.api.dto.StreamSensorEvent;
import org.csi.yucca.gateway.configuration.StreamConfigurationDAO;
import org.csi.yucca.gateway.configuration.YuccaSettings;
import org.csi.yucca.gateway.exception.InsertApiBaseException;
import org.csi.yucca.gateway.util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromMqttActivator {
	private static final Logger log=Logger.getLogger(FromMqttActivator.class);

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private YuccaSettings yuccaSettings;
	
	@Autowired
	private YuccaLikeService yuccaLikeService;

	@Autowired
	private StreamConfigurationDAO streamConfigurationDAO;
	
	
	 public void manageMqttMessage(byte[] payload) {
		 	String eventStr = new String(payload);
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode event = null;
			try {
				event = mapper.readTree(eventStr);
			} catch (Exception e) {
				ErrorOnInbound errorOnInbound = new ErrorOnInbound();
				errorOnInbound.setError_code("E012");
				errorOnInbound.setError_name("Json validation failed");
				errorOnInbound.setMessage(eventStr);
				errorOnInbound.setOutput("output."+yuccaSettings.getTenant().getCode()+".errors");
				sendError(errorOnInbound);
				return;
			}

			StreamSensorEvent eventDto = null;
			try {
				eventDto = InputManager.validate(yuccaSettings.getTenant().getCode(), event, streamConfigurationDAO);
			} catch (InsertApiBaseException e) {
				ErrorOnInbound errorOnInbound = new ErrorOnInbound();
				errorOnInbound.setError_code(e.getErrorCode());
				errorOnInbound.setError_name(e.getErrorName());
				errorOnInbound.setMessage(eventStr);
				errorOnInbound.setOutput("output."+yuccaSettings.getTenant().getCode()+".errors");
				sendError(errorOnInbound);
				return;
			}
			try {
				yuccaLikeService.sendEventToYucca(Conversion.fromStreamSensorEvent2EventMessage(eventDto));
			} catch (JsonProcessingException e) {
				ErrorOnInbound errorOnInbound = new ErrorOnInbound();
				errorOnInbound.setError_code("E012");
				errorOnInbound.setError_name("Json validation failed");
				errorOnInbound.setMessage(eventStr);
				errorOnInbound.setOutput("output."+yuccaSettings.getTenant().getCode()+".errors");
				sendError(errorOnInbound);
				return;
			}
					
		 
  }


	private void sendError(ErrorOnInbound errorOnInbound) {
		jmsTemplate.send(new ActiveMQTopic(errorOnInbound.getOutput()) ,new TextMessageCreator(errorOnInbound.toString())); 
	}
	 

	private class TextMessageCreator implements MessageCreator {
		
		private String text;
		public TextMessageCreator(String text) {
			this.text = text;
		}
		
		@Override
		public Message createMessage(Session session) throws JMSException {
			ActiveMQTextMessage msg = new ActiveMQTextMessage();
			msg.setText(text);
			return msg;
		}
	}
	
	 
}

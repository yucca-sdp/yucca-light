/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.csi.yucca.gateway.integration.dto.EventMessage;
import org.csi.yucca.gateway.integration.dto.MeasureWithRef;
import org.csi.yucca.gateway.integration.util.HeaderNameEnum;
import org.csi.yucca.gateway.util.Conversion;
import org.csi.yucca.gateway.util.EventStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.history.MessageHistory;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.IdGenerator;

public class AggregatorEventForA2A implements CorrelationStrategy {

	@Autowired
	private MessageBuilderFactory messageBuilderFactory;
	
	
	public Message<?> aggregate(List<Message<?>> messages) {
		EventMessage msg = new EventMessage();
		List<MeasureWithRef> measuresList = new ArrayList();

		for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
			Message<?> message = (Message<?>) iterator.next();
			String gwId = ((java.util.UUID)message.getHeaders().get(HeaderNameEnum.gwId.name())).toString();
			EventMessage dto = (EventMessage) message.getPayload();
			Conversion.addRefToMeasureWithRef(gwId, dto.getMeasures());
			
			measuresList.addAll(dto.getMeasures());
			if (!iterator.hasNext())
			{
				msg.setApplication(dto.isApplication());
				msg.setSourceCode(dto.getSourceCode());
				msg.setStreamCode(dto.getStreamCode());
				msg.setMeasures(measuresList);
			}
				
		}
		
		
		return messageBuilderFactory.withPayload(msg).copyHeaders(aggregatedHeaders(messages)).build();
	}	

	 public Object getCorrelationKey(Message<?> message)
	 {
		 if (message.getPayload() instanceof EventMessage)
			 return ((EventMessage) message.getPayload()).getSourceCode()+"_"+((EventMessage) message.getPayload()).getStreamCode();
		 else 
			 return message.getHeaders().get("CORRELATION_ID");
	 }
	
	
     private Map<String, ?> aggregatedHeaders(List<Message<?>> messages) {
    	Map<String, Object> headers = new HashMap<String, Object>();

		Map<String, Map<String,Object>> allHeaders = new HashMap();
		for (Message<?> message : messages) {
			Map<String,Object> heads = new HashMap<>();
			java.util.UUID gwId = (java.util.UUID)message.getHeaders().get(HeaderNameEnum.gwId.name());
			heads.put(HeaderNameEnum.gwId.name(), gwId);
			heads.put(HeaderNameEnum.gwTimestamp.name(), message.getHeaders().get(HeaderNameEnum.gwTimestamp.name()));
			heads.put(HeaderNameEnum.gwAttemptId.name(), message.getHeaders().get(HeaderNameEnum.gwAttemptId.name()));
			heads.put(MessageHistory.HEADER_NAME, message.getHeaders().get(MessageHistory.HEADER_NAME));
			allHeaders.put(gwId.toString(),heads);
		}
		
		headers.put("messagesHeaders", allHeaders);
		headers.put(HeaderNameEnum.gwAttemptId.name(), java.util.UUID.randomUUID());
		
		return headers;
	}

}

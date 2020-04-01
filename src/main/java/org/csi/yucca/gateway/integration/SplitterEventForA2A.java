/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.csi.yucca.gateway.integration.dto.EventMessage;
import org.csi.yucca.gateway.integration.dto.MeasureWithRef;
import org.csi.yucca.gateway.integration.util.HeaderNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.history.MessageHistory;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class SplitterEventForA2A {

	@Autowired
	private MessageBuilderFactory messageBuilderFactory;

	
	public List<Message<?>> split(Message<?> message) {
		EventMessage dto = (EventMessage) message.getPayload();
		
		Map<String, EventMessage> messaggiOut = new HashMap<>();
		List<Message<?>> list = new ArrayList<>();
		
		if (dto!=null && dto.getMeasures()!=null)
		{
			for (MeasureWithRef misura : dto.getMeasures()) {
				EventMessage msgPayload = messaggiOut.get(misura.getRefId());
				if (msgPayload==null)
				{
					msgPayload = new EventMessage();
					messaggiOut.put(misura.getRefId(), msgPayload);
				}
				msgPayload.setApplication(dto.isApplication());
				msgPayload.setSourceCode(dto.getSourceCode());
				msgPayload.setStreamCode(dto.getStreamCode());
				if (msgPayload.getMeasures()==null)
					msgPayload.setMeasures(new ArrayList());
				msgPayload.getMeasures().add(misura);
			}

			for (Map.Entry<String,EventMessage> entry : messaggiOut.entrySet()) {
				Message<?> msg = messageBuilderFactory.withPayload(entry.getValue()).copyHeaders(splitHeaders(message.getHeaders(),entry.getKey())).build();
				list.add(msg);
			}
		
		}
		
		
		 return list;
	}


	private Map<String, ?> splitHeaders(MessageHeaders multiHeaders, String key) {
		Map<String, Object> singleHeaders = new HashMap<>();
		
		Map<String, Object> singleHeadersFromMulti = (Map<String, Object>)((Map<String, Object>)multiHeaders.get("messagesHeaders")).get(key);

		singleHeaders.put(HeaderNameEnum.gwId.name(), singleHeadersFromMulti.get(HeaderNameEnum.gwId.name()));
		singleHeaders.put(HeaderNameEnum.gwTimestamp.name(), singleHeadersFromMulti.get(HeaderNameEnum.gwTimestamp.name()));
		singleHeaders.put(MessageHistory.HEADER_NAME, singleHeadersFromMulti.get(MessageHistory.HEADER_NAME));
		
		singleHeaders.put(HeaderNameEnum.gwAttemptId.name(), singleHeadersFromMulti.get(HeaderNameEnum.gwAttemptId.name()));
		singleHeaders.put(HeaderNameEnum.gwAttemptResponse.name(), multiHeaders.get(HeaderNameEnum.gwAttemptResponse.name()));
		singleHeaders.put(HeaderNameEnum.gwAttemptResponseDetail.name(), multiHeaders.get(HeaderNameEnum.gwAttemptResponseDetail.name()));
		singleHeaders.put(HeaderNameEnum.gwStatus.name(), multiHeaders.get(HeaderNameEnum.gwStatus.name()));
		
		return singleHeaders;
	}



}

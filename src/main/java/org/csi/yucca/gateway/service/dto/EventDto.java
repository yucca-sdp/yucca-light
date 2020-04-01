/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service.dto;

//import org.apache.commons.lang3.StringUtils;
//import org.dozer.Mapping;

public class EventDto {
	
	public EventDto(){
		
	}

    private String eventId;
	
	private Long eventInsertTimestamp;

    private String eventStatus;

    private String eventSourceCode;

    private String eventStreamCode;

    private Boolean eventIsApp;

    private String eventJSON;

    private String eventLastAttempiID;

    private Long eventLastAttemptSendTimestamp;

    private Long eventLastAttemptReceiveTimestamp;

    private String eventLastResponse;

    private String eventLastEndpoint;
    
    private Long eventNumAttempt;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Long getEventInsertTimestamp() {
		return eventInsertTimestamp;
	}

	public void setEventInsertTimestamp(Long eventInsertTimestamp) {
		this.eventInsertTimestamp = eventInsertTimestamp;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventSourceCode() {
		return eventSourceCode;
	}

	public void setEventSourceCode(String eventSourceCode) {
		this.eventSourceCode = eventSourceCode;
	}

	public String getEventStreamCode() {
		return eventStreamCode;
	}

	public void setEventStreamCode(String eventStreamCode) {
		this.eventStreamCode = eventStreamCode;
	}

	public Boolean getEventIsApp() {
		return eventIsApp;
	}

	public void setEventIsApp(Boolean eventIsApp) {
		this.eventIsApp = eventIsApp;
	}

	public String getEventJSON() {
		return eventJSON;
	}

	public void setEventJSON(String eventJSON) {
		this.eventJSON = eventJSON;
	}

	public String getEventLastAttempiID() {
		return eventLastAttempiID;
	}

	public void setEventLastAttempiID(String eventLastAttempiID) {
		this.eventLastAttempiID = eventLastAttempiID;
	}

	public Long getEventLastAttemptSendTimestamp() {
		return eventLastAttemptSendTimestamp;
	}

	public void setEventLastAttemptSendTimestamp(Long eventLastAttemptSendTimestamp) {
		this.eventLastAttemptSendTimestamp = eventLastAttemptSendTimestamp;
	}

	public Long getEventLastAttemptReceiveTimestamp() {
		return eventLastAttemptReceiveTimestamp;
	}

	public void setEventLastAttemptReceiveTimestamp(Long eventLastAttemptReceiveTimestamp) {
		this.eventLastAttemptReceiveTimestamp = eventLastAttemptReceiveTimestamp;
	}

	public String getEventLastResponse() {
		return eventLastResponse;
	}

	public void setEventLastResponse(String eventLastResponse) {
		this.eventLastResponse = eventLastResponse;
	}

	public String getEventLastEndpoint() {
		return eventLastEndpoint;
	}

	public void setEventLastEndpoint(String eventLastEndpoint) {
		this.eventLastEndpoint = eventLastEndpoint;
	}

	public Long getEventNumAttempt() {
		return eventNumAttempt;
	}

	public void setEventNumAttempt(Long eventNumAttempt) {
		this.eventNumAttempt = eventNumAttempt;
	}
}

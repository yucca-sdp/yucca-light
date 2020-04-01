/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service.dto;

import java.io.Serializable;

public class AttemptDto {

    private String attemptGWId;
	
	private String attemptId;

    private Long attemptSendTimestamp;

    private Long attemptReceiveTimestamp;
    
    private String attemptFromStatus;
    
    private String attemptToStatus;

    private String attemptResponse;
    
    private String attemptResponseDetail;

    private String attemptEndPoint;

	public String getAttemptGWId() {
		return attemptGWId;
	}

	public void setAttemptGWId(String attemptGWId) {
		this.attemptGWId = attemptGWId;
	}

	public String getAttemptId() {
		return attemptId;  
	}

	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	public Long getAttemptSendTimestamp() {
		return attemptSendTimestamp;
	}

	public void setAttemptSendTimestamp(Long attemptSendTimestamp) {
		this.attemptSendTimestamp = attemptSendTimestamp;
	}

	public Long getAttemptReceiveTimestamp() {
		return attemptReceiveTimestamp;
	}

	public void setAttemptReceiveTimestamp(Long attemptReceiveTimestamp) {
		this.attemptReceiveTimestamp = attemptReceiveTimestamp;
	}

	public String getAttemptResponse() {
		return attemptResponse;
	}

	public void setAttemptResponse(String attemptResponse) {
		this.attemptResponse = attemptResponse;
	}

	public String getAttemptEndPoint() {
		return attemptEndPoint;
	}

	public void setAttemptEndPoint(String attemptEndPoint) {
		this.attemptEndPoint = attemptEndPoint;
	}

	public String getAttemptFromStatus() {
		return attemptFromStatus;
	}

	public void setAttemptFromStatus(String attemptFromStatus) {
		this.attemptFromStatus = attemptFromStatus;
	}

	public String getAttemptToStatus() {
		return attemptToStatus;
	}

	public void setAttemptToStatus(String attemptToStatus) {
		this.attemptToStatus = attemptToStatus;
	}

	public String getAttemptResponseDetail() {
		return attemptResponseDetail;
	}

	public void setAttemptResponseDetail(String attemptResponseDetail) {
		this.attemptResponseDetail = attemptResponseDetail;
	}

	public static class AttemptKey implements Serializable{
		
		private static final long serialVersionUID = 2L;

		protected AttemptKey () {
			
		}
		
		protected AttemptKey (String GW_ID, String ATTEMPT_ID) {
			this.GW_ID=GW_ID;
			this.ATTEMPT_ID=ATTEMPT_ID;
		}
		
		private String GW_ID;
		
		private String ATTEMPT_ID;

		public String getGW_ID() {
			return GW_ID;
		}

		public void setGW_ID(String gW_ID) {
			GW_ID = gW_ID;
		}

		public String getATTEMPT_ID() {
			return ATTEMPT_ID;
		}

		public void setATTEMPT_ID(String aTTEMPT_ID) {
			ATTEMPT_ID = aTTEMPT_ID;
		}
	}
}

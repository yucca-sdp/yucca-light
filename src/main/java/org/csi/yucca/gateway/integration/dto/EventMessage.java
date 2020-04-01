/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class EventMessage implements Serializable{

	private String sourceCode;	
	private String streamCode;
	private boolean application;
	private List<MeasureWithRef> measures;
	public boolean isApplication() {
		return application;
	}


	public void setApplication(boolean application) {
		this.application = application;
	}




	public EventMessage() {
	}
	
	
	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}


	public List<MeasureWithRef> getMeasures() {
		return measures;
	}

	public void setMeasures(List<MeasureWithRef> measures) {
		this.measures = measures;
	}


	public String toString() {
		StringBuilder str = new StringBuilder("{ \"stream\":\"").append(getStreamCode()).append("\",");
		str.append("\"").append(isApplication()?"application\":\"":"sensor\":\"").append(getSourceCode()).append("\"");
		str.append(",\"values\":[");
		str.append(StringUtils.join(measures, ','));
		str.append("]}");
		return str.toString();
	}


}

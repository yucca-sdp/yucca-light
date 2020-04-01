/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api.dto;

import org.apache.commons.lang.StringUtils;

public class ErrorOnInbound {

	private String error_name;
	private String error_code;
	private String output;
	private String message;
	
	
	public String getError_name() {
		return error_name;
	}
	public void setError_name(String error_name) {
		this.error_name = error_name;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("{ \"error_name\":\"").append(getError_name()).append("\",");
		str.append("\"").append("error_code\":\"").append(getError_code()).append("\",");
		str.append("\"").append("output\":\"").append(getOutput()).append("\",");
		str.append("\"").append("message\":\"").append(getMessage()).append("\"");
		str.append("}");
		return str.toString();

	}
}

/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api.dto;

import java.util.Map;

public class Measure {

	private String time;
	private Map<String, Object> components;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Map<String, Object> getComponents() {
		return components;
	}
	public void setComponents(Map<String, Object> components) {
		this.components = components;
	}
	
	
}

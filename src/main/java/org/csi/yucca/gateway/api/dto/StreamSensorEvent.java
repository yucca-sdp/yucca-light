/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class StreamSensorEvent  {
	
	private String stream;	
	
	private String application;
	private String sensor;
	
	private Measure[] values;
	
	
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public Measure[] getValues() {
		return values;
	}
	public void setValues(Measure[] values) {
		this.values = values;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	
	@JsonIgnore
	public String getSource()
	{
		return  ((getSensor()!=null)?getSensor():getApplication());
	}

	@JsonIgnore
	public String getSchema()
	{
		String source = getSource(); 
		return ""+source==null?"":source+"_"+getStream()+"_schema.json";
	}
	
}

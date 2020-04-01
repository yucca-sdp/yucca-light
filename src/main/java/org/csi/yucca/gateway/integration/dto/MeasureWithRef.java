/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration.dto;

import java.io.Serializable;

public class MeasureWithRef implements Serializable{

	private String refId;
	private String value;

	public MeasureWithRef(String value) {
		this.value = value;
	}

	public MeasureWithRef(String value, String refId) {
		this.value = value;
		this.refId = refId;
	}

	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}

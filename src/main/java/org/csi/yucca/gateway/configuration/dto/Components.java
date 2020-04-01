/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration.dto;

import java.util.List;

public class Components {
	private List<Element> element;

	public Components() {
		super();
	}

	public List<Element> getElement() {
		return element;
	}

	public void setElement(List<Element> element) {
		this.element = element;
	}

}

/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import org.springframework.core.convert.converter.Converter;

public class ObjectToA2AStringConverter implements Converter<Object, String> {

		@Override
		public String convert(Object source) {
			return "["+source.toString()+"]";
		}
}


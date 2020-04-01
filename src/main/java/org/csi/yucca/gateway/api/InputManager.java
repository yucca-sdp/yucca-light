/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api;

import java.io.IOException;
import java.util.Map;

import org.csi.yucca.gateway.api.dto.StreamSensorEvent;
import org.csi.yucca.gateway.configuration.StreamConfigurationDAO;
import org.csi.yucca.gateway.exception.InsertApiBaseException;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import ch.qos.logback.classic.spi.ThrowableProxyVO;

public class InputManager {

	public static StreamSensorEvent validate(String tenant, JsonNode event, StreamConfigurationDAO streamConfigurationDAO) throws InsertApiBaseException {

		boolean isOk = false;

		try {
			isOk = ParseValidationUtil.isValidVersusSchema(event, ParseValidationUtil.GENERIC_SCHEMA);
		} catch (Throwable e3) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, event.toString());
		}

		if (!isOk)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, event.toString());

		Jackson2JsonObjectMapper mapper = new Jackson2JsonObjectMapper();
		StreamSensorEvent eventDto = null;
		try {
			eventDto = mapper.fromJson(event.toString(), StreamSensorEvent.class);
		} catch (Exception e1) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, event.toString());
		}

		if (eventDto.getStream() == null || eventDto.getStream().equals("")) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE, event.toString());
		}
		if ((eventDto.getApplication() == null || eventDto.getApplication().equals("")) && (eventDto.getSensor() == null || eventDto.getSensor().equals(""))) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE, event.toString());
		}

		isOk = ParseValidationUtil.validateEventTime(eventDto);
		if (!isOk)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INVALID_COMPONENTS, event.toString());

		try {
			isOk = ParseValidationUtil.isValidVersusSchema(event, tenant, eventDto.getStream(), eventDto.getSource(), streamConfigurationDAO);
		} catch (InsertApiBaseException | ProcessingException | IOException e) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, event.toString());
		}

		if (!isOk)
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INVALID_COMPONENTS, event.toString());

		return eventDto;

	}

	
}

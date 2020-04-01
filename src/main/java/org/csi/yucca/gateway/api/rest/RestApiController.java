/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.csi.yucca.gateway.api.InputManager;
import org.csi.yucca.gateway.api.dto.ErrorOnInbound;
import org.csi.yucca.gateway.api.dto.StreamSensorEvent;
import org.csi.yucca.gateway.configuration.StreamConfigurationDAO;
import org.csi.yucca.gateway.exception.InsertApiBaseException;
import org.csi.yucca.gateway.integration.YuccaLikeService;
import org.csi.yucca.gateway.util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@RestController
@Scope("request")
@RequestMapping(value = "/api")
public class RestApiController {

	Logger logger = Logger.getLogger("RestApiController");
	
	@Autowired
	private YuccaLikeService yuccaLikeService;

	@Autowired
	private StreamConfigurationDAO streamConfigurationDAO;

	
	
	@RequestMapping(value = "/input/{tenant}")
	public ResponseEntity receive(@PathVariable(value = "tenant") String tenant,
			@RequestBody String  eventStr) throws InsertApiBaseException, JsonProcessingException,IOException  {

		ObjectMapper mapper = new ObjectMapper();
		
		
		JsonNode event = mapper.readTree(eventStr);		
		
		StreamSensorEvent eventDto =InputManager.validate(tenant, event, streamConfigurationDAO);
		try {
			yuccaLikeService.sendEventToYucca(Conversion.fromStreamSensorEvent2EventMessage(eventDto));
		} catch (JsonProcessingException e) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,eventStr);
		}
				
		
		return new ResponseEntity(HttpStatus.ACCEPTED);
	}
	

	public void setYuccaLikeService(YuccaLikeService service)
	{
		this.yuccaLikeService = service;
	}


	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InsertApiBaseException.class)
	@ResponseBody ErrorOnInbound handleBadRequest(HttpServletRequest req, InsertApiBaseException e) {
		ErrorOnInbound errorOnInbound = new ErrorOnInbound();
		errorOnInbound.setError_code(e.getErrorCode());
		errorOnInbound.setError_name(e.getErrorName());
		errorOnInbound.setMessage(e.getMessage());
		return errorOnInbound;
	} 
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(JsonProcessingException.class)
	@ResponseBody ErrorOnInbound handleJsonProcessingException(HttpServletRequest req, JsonProcessingException e) throws IOException {
		ErrorOnInbound errorOnInbound = new ErrorOnInbound();
		errorOnInbound.setError_code("E012");
		errorOnInbound.setError_name("Json validation failed");
		errorOnInbound.setMessage(e.getLocation().getSourceRef().toString());
		return errorOnInbound;
	} 

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(PropertyBindingException.class)
	@ResponseBody ErrorOnInbound handleJsonProcessingException(HttpServletRequest req, PropertyBindingException e) throws IOException {
		ErrorOnInbound errorOnInbound = new ErrorOnInbound();
		errorOnInbound.setError_code("E012");
		errorOnInbound.setError_name("Json validation failed");
		errorOnInbound.setMessage(e.getOriginalMessage());
		return errorOnInbound;
	} 


}

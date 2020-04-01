/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class StreamConfiguration {
	private String streamCode;
	private String streamName;
	private ConfigData configData;
	private Streams streams;
	@JsonIgnore
	private String jsonMetadata;

	public StreamConfiguration() {
		super();
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public ConfigData getConfigData() {
		return configData;
	}

	public void setConfigData(ConfigData configData) {
		this.configData = configData;
	}

	public Streams getStreams() {
		return streams;
	}

	public void setStreams(Streams streams) {
		this.streams = streams;
	}

	@JsonIgnore
	public String getJSonSchema() {
		if (getStreams() == null)
			setStreams(new Streams());
		if (getStreams().getStream() == null)
			getStreams().setStream(new Stream());

		String virtualEntityTYpe = getStreams().getStream().getVirtualEntityType().equalsIgnoreCase("Application") ? "application" : "sensor";

		String jsonSchema = "{";
		jsonSchema += "\"title\": \"" + getStreamCode() + " sensor schema\",";
		jsonSchema += "\"type\": \"object\",";
		jsonSchema += "\"properties\": {";
		jsonSchema += "\"stream\": {";
		jsonSchema += "\"type\": \"string\"";
		jsonSchema += "},";
		jsonSchema += "\"" + virtualEntityTYpe + "\": {";
		jsonSchema += "\"type\": \"string\"";
		jsonSchema += "},";
		jsonSchema += "\"values\": {";
		jsonSchema += "\"type\": \"array\",";
		jsonSchema += "\"minItems\" : 1,";
		jsonSchema += "\"items\": { ";
		jsonSchema += "\"type\": \"object\",";
		jsonSchema += "\"properties\": {";
		jsonSchema += "\"time\": {";
		jsonSchema += "\"type\": \"string\"";
		jsonSchema += "},";
		jsonSchema += "\"components\" : {";
		jsonSchema += "\"type\": \"object\",";
		jsonSchema += "\"properties\": {";

		List<String> requredComponents = new ArrayList<>();
		if (getStreams().getStream().getComponents() != null && getStreams().getStream().getComponents().getElement() != null) {
			int counter=0;
			int size = getStreams().getStream().getComponents().getElement().size();
			for (Element component : getStreams().getStream().getComponents().getElement()) {
				counter++;
				String separator = counter < size ? "," : "";
				jsonSchema += "\"" + component.getComponentName() + "\" : {";

				if ("float".equals(component.getDataType()) || "double".equals(component.getDataType()) || "longitude".equals(component.getDataType())
						|| "latitude".equals(component.getDataType())) {
					jsonSchema += "\"anyOf\": [ {\"type\": \"string\" , \"pattern\":\"^[-+]?[0-9]*\\\\.?[0-9]+$\"},{\"type\": \"number\"}]  ";
				} else if ("int".equals(component.getDataType())|| "long".equals(component.getDataType()) ) {
					jsonSchema += "\"anyOf\": [ {\"type\": \"string\" , \"pattern\":\"^[-+]?[0-9]+$\"},{\"type\": \"integer\"}]  "; 
				} else if ("dateTime".equals(component.getDataType())) {
					jsonSchema += "\"type\" : \"string\",  \"pattern\": \"((000[1-9])|(00[1-9][0-9])|(0[1-9][0-9]{2})|([1-9][0-9]{3}))-((0[1-9])|(1[012]))-((0[1-9])|([12][0-9])|(3[01]))T(([01][0-9])|(2[0-3]))(:[0-5][0-9]){2}(\\\\.[0-9]+)?(([\\\\+|\\\\-]((0[0-9])|(1[0-2]))(:[0-5][0-9]))|(\\\\+13(:[0-5][0-9])(:[0-5][0-9]))|\\\\+14:00|Z|([\\\\+|\\\\-]((0[0-9])|(1[0-2]))([0-5][0-9]))|(\\\\+13([0-5][0-9])([0-5][0-9]))|\\\\+1400)\"";
				} else
					jsonSchema += "\"type\" : \"" + component.getDataType() + "\"";

				jsonSchema += "}" + separator;
				if (component.getSinceVersion()==null || component.getSinceVersion().equals(1))
					requredComponents.add("\"" + component.getComponentName() + "\"");
			}
		}
		jsonSchema += "},";
		jsonSchema += "\"required\": [" + StringUtils.join(requredComponents, ",")+ "]";
		jsonSchema += "}";
		jsonSchema += "}";
		jsonSchema += "}";
		jsonSchema += "}";
		jsonSchema += "},";
		jsonSchema += "\"required\": [\"stream\", \"" + virtualEntityTYpe + "\", \"values\"]";
		jsonSchema += "}";
		// https://github.com/csipiemonte/yucca-realtime/blob/master/stream_template_cepartifacts/cepconfig/src/main/esbconfig/api__deploy__tenant__sensor__stream__addTextResource.xml
		return jsonSchema;

	}

	public String getJsonMetadata() {
		return jsonMetadata;
	}

	public void setJsonMetadata(String jsonMetadata) {
		this.jsonMetadata = jsonMetadata;
	}

}

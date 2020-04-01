/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration.dto;

public class StreamMetadata {
	private String tenantCode;
	private String streamCode;
	private String virtualentityCode;
	private Long deploymentVersion;
	private String streamName;
	private String virtualentityName;
	private String virtualentityDescription;
	private String virtualentityType;
	private String virtualentityCategory;
	private Long lastupdateTimestamp;
	private String metadataJson;
	private String schemaJson;

	public StreamMetadata() {
		super();
	}

	public StreamMetadata(String tenantCode, String streamCode, String virtualentityCode, Long deploymentVersion, String streamName, String virtualentityName, String virtualentityDescription,
			String virtualentityType, String virtualentityCategory, Long lastupdateTimestamp, String metadataJson, String schemaJson) {
		super();
		this.tenantCode = tenantCode;
		this.streamCode = streamCode;
		this.virtualentityCode = virtualentityCode;
		this.deploymentVersion = deploymentVersion;
		this.streamName = streamName;
		this.virtualentityName = virtualentityName;
		this.virtualentityDescription = virtualentityDescription;
		this.virtualentityType = virtualentityType;
		this.virtualentityCategory = virtualentityCategory;
		this.lastupdateTimestamp = lastupdateTimestamp;
		this.metadataJson = metadataJson;
		this.schemaJson = schemaJson;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}

	public String getVirtualentityCode() {
		return virtualentityCode;
	}

	public void setVirtualentityCode(String virtualentityCode) {
		this.virtualentityCode = virtualentityCode;
	}

	public Long getDeploymentVersion() {
		return deploymentVersion;
	}

	public void setDeploymentVersion(Long deploymentVersion) {
		this.deploymentVersion = deploymentVersion;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getVirtualentityName() {
		return virtualentityName;
	}

	public void setVirtualentityName(String virtualentityName) {
		this.virtualentityName = virtualentityName;
	}

	public String getVirtualentityDescription() {
		return virtualentityDescription;
	}

	public void setVirtualentityDescription(String virtualentityDescription) {
		this.virtualentityDescription = virtualentityDescription;
	}

	public String getVirtualentityType() {
		return virtualentityType;
	}

	public void setVirtualentityType(String virtualentityType) {
		this.virtualentityType = virtualentityType;
	}

	public String getVirtualentityCategory() {
		return virtualentityCategory;
	}

	public void setVirtualentityCategory(String virtualentityCategory) {
		this.virtualentityCategory = virtualentityCategory;
	}

	public Long getLastupdateTimestamp() {
		return lastupdateTimestamp;
	}

	public void setLastupdateTimestamp(Long lastupdateTimestamp) {
		this.lastupdateTimestamp = lastupdateTimestamp;
	}

	public String getMetadataJson() {
		return metadataJson;
	}

	public void setMetadataJson(String metadataJson) {
		this.metadataJson = metadataJson;
	}

	public String getSchemaJson() {
		return schemaJson;
	}

	public void setSchemaJson(String schemaJson) {
		this.schemaJson = schemaJson;
	}

}

/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service.dto;

import java.io.Serializable;

public class StreamMetadataDto {
	
	public StreamMetadataDto(){
	}

	private String tenantCode;
	
	private String streamCode;

	private String virtualEntityCode;

	private Long deploymentVersion;
	
	private String streamName;

	private String virtualEntityName;

	private String virtualEntityDescription;

	private String virtualEntityType;

	private String virtualEntityCategory;

	private Long lastUpdateTimestamp;

	private String metadataJson;

	private String schemaJson;

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

	public String getVirtualEntityCode() {
		return virtualEntityCode;
	}

	public void setVirtualEntityCode(String virtualEntityCode) {
		this.virtualEntityCode = virtualEntityCode;
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

	public String getVirtualEntityName() {
		return virtualEntityName;
	}

	public void setVirtualEntityName(String virtualEntityName) {
		this.virtualEntityName = virtualEntityName;
	}

	public String getVirtualEntityDescription() {
		return virtualEntityDescription;
	}

	public void setVirtualEntityDescription(String virtualEntityDescription) {
		this.virtualEntityDescription = virtualEntityDescription;
	}

	public String getVirtualEntityType() {
		return virtualEntityType;
	}

	public void setVirtualEntityType(String virtualEntityType) {
		this.virtualEntityType = virtualEntityType;
	}

	public String getVirtualEntityCategory() {
		return virtualEntityCategory;
	}

	public void setVirtualEntityCategory(String virtualEntityCategory) {
		this.virtualEntityCategory = virtualEntityCategory;
	}

	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Long lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
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

	public static class MetadataKey implements Serializable{
		
		private static final long serialVersionUID = 2L;

		protected MetadataKey () {
			
		}
		
		protected MetadataKey (String TENANT_CODE,String STREAM_CODE,String VIRTUALENTITY_CODE,Long DEPLOYMENT_VERSION) {
			this.TENANT_CODE=TENANT_CODE;
			this.STREAM_CODE=STREAM_CODE;
			this.VIRTUALENTITY_CODE=VIRTUALENTITY_CODE;
			this.DEPLOYMENT_VERSION=DEPLOYMENT_VERSION;
		}
		
		private String TENANT_CODE;
		
		private String STREAM_CODE;

		private String VIRTUALENTITY_CODE;

		private Long DEPLOYMENT_VERSION;

		public String getTENANT_CODE() {
			return TENANT_CODE;
		}

		public void setTENANT_CODE(String TENANT_CODE) {
			TENANT_CODE = TENANT_CODE;
		}

		public String getSTREAM_CODE() {
			return STREAM_CODE;
		}

		public void setSTREAM_CODE(String STREAM_CODE) {
			STREAM_CODE = STREAM_CODE;
		}

		public String getVIRTUALENTITY_CODE() {
			return VIRTUALENTITY_CODE;
		}

		public void setVIRTUALENTITY_CODE(String VIRTUALENTITY_CODE) {
			VIRTUALENTITY_CODE = VIRTUALENTITY_CODE;
		}

		public Long getDEPLOYMENT_VERSION() {
			return DEPLOYMENT_VERSION;
		}

		public void setDEPLOYMENT_VERSION(Long DEPLOYMENT_VERSION) {
			DEPLOYMENT_VERSION = DEPLOYMENT_VERSION;
		}	
		
	}
}

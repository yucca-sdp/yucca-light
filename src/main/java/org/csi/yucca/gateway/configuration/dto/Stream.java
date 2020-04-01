/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Stream {

	private String virtualEntityName;
	private String virtualEntityDescription;
	private String virtualEntityCode;
	private String virtualEntityType;
	private String virtualEntityCategory;
	private String domainStream;
	private String licence;
	private String disclaimer;
	private String copyright;
	private String visibility;
	private String deploymentVersion;
	private String streamIcon;
	private Components components;
	private StreamTags streamTags;
	private VirtualEntityPositions virtualEntityPositions;

	public Stream() {
		super();
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

	public String getVirtualEntityCode() {
		return virtualEntityCode;
	}

	public void setVirtualEntityCode(String virtualEntityCode) {
		this.virtualEntityCode = virtualEntityCode;
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

	public String getDomainStream() {
		return domainStream;
	}

	public void setDomainStream(String domainStream) {
		this.domainStream = domainStream;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getDeploymentVersion() {
		return deploymentVersion;
	}

	public void setDeploymentVersion(String deploymentVersion) {
		this.deploymentVersion = deploymentVersion;
	}

	public String getStreamIcon() {
		return streamIcon;
	}

	public void setStreamIcon(String streamIcon) {
		this.streamIcon = streamIcon;
	}

	public Components getComponents() {
		return components;
	}

	public void setComponents(Components components) {
		this.components = components;
	}

	public StreamTags getStreamTags() {
		return streamTags;
	}

	public void setStreamTags(StreamTags streamTags) {
		this.streamTags = streamTags;
	}

	public VirtualEntityPositions getVirtualEntityPositions() {
		return virtualEntityPositions;
	}

	public void setVirtualEntityPositions(VirtualEntityPositions virtualEntityPositions) {
		this.virtualEntityPositions = virtualEntityPositions;
	}

}

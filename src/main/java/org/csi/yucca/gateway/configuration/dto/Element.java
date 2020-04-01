/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration.dto;

public class Element {

	private String componentName;
	private String componentAlias;
	private String tolerance;
	private String measureUnit;
	private String measureUnitCategory;
	private String phenomenon;
	private String phenomenonCategory;
	private String dataType;
	private Integer sinceVersion;
	
	public Element() {
		super();
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentAlias() {
		return componentAlias;
	}

	public void setComponentAlias(String componentAlias) {
		this.componentAlias = componentAlias;
	}

	public String getTolerance() {
		return tolerance;
	}

	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getMeasureUnitCategory() {
		return measureUnitCategory;
	}

	public void setMeasureUnitCategory(String measureUnitCategory) {
		this.measureUnitCategory = measureUnitCategory;
	}

	public String getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}

	public String getPhenomenonCategory() {
		return phenomenonCategory;
	}

	public void setPhenomenonCategory(String phenomenonCategory) {
		this.phenomenonCategory = phenomenonCategory;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getSinceVersion() {
		return sinceVersion;
	}

	public void setSinceVersion(Integer sinceVersion) {
		this.sinceVersion = sinceVersion;
	}

}

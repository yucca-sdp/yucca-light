/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StreamMetadataRowMapper implements RowMapper<StreamMetadata> {

	@Override
	public StreamMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
		StreamMetadata streamMetadata = new StreamMetadata();
		streamMetadata = new StreamMetadata();
		streamMetadata.setTenantCode(rs.getString("TENANT_CODE"));
		streamMetadata.setStreamCode(rs.getString("STREAM_CODE"));
		streamMetadata.setVirtualentityCode(rs.getString("VIRTUALENTITY_CODE"));
		streamMetadata.setDeploymentVersion(rs.getLong("DEPLOYMENT_VERSION"));
		streamMetadata.setStreamName(rs.getString("STREAM_NAME"));
		streamMetadata.setVirtualentityName(rs.getString("VIRTUALENTITY_NAME"));
		streamMetadata.setVirtualentityDescription(rs.getString("VIRTUALENTITY_DESCRIPTION"));
		streamMetadata.setVirtualentityType(rs.getString("VIRTUALENTITY_TYPE"));
		streamMetadata.setVirtualentityCategory(rs.getString("VIRTUALENTITY_CATEGORY"));
		streamMetadata.setLastupdateTimestamp(rs.getLong("LASTUPDATE_TIMESTAMP"));
		streamMetadata.setMetadataJson(rs.getString("METADATA_JSON"));
		streamMetadata.setSchemaJson(rs.getString("SCHEMA_JSON"));
		return streamMetadata;

	}

}

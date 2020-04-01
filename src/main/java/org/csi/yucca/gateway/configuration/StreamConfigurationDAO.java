/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration;

import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.gateway.configuration.dto.StreamConfiguration;
import org.csi.yucca.gateway.configuration.dto.StreamMetadata;
import org.csi.yucca.gateway.configuration.dto.StreamMetadataRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class StreamConfigurationDAO {

	private static final Logger log = Logger.getLogger("org.csi.yucca.datainsert");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveStreamMetadataConfiguration(StreamConfiguration streamConfiguration, String streamJson) {

		log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - START ");
		String tenantCode = streamConfiguration.getConfigData().getTenantCode();
		String streamCode = streamConfiguration.getStreamCode();
		String virtualEntityCode = streamConfiguration.getStreams().getStream().getVirtualEntityCode();
		Long deploymentVersion = new Long(streamConfiguration.getStreams().getStream().getDeploymentVersion());

		log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - tenantCode: " + tenantCode + " | streamCode " + streamCode + " | virtualEntityCode " + virtualEntityCode
				+ " | deploymentVersion " + deploymentVersion);

		StreamMetadata existingStreamMetadata = findStreamMetadataConfigurationByPk(tenantCode, streamCode, virtualEntityCode, deploymentVersion);

		if (existingStreamMetadata == null) {

			String sql = "INSERT INTO STREAM_METADATA_CONFIGURATION (TENANT_CODE, STREAM_CODE, VIRTUALENTITY_CODE, DEPLOYMENT_VERSION, STREAM_NAME, VIRTUALENTITY_NAME, "
					+ "VIRTUALENTITY_DESCRIPTION, VIRTUALENTITY_TYPE, VIRTUALENTITY_CATEGORY, LASTUPDATE_TIMESTAMP, METADATA_JSON, SCHEMA_JSON) VALUES (?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?)";

			log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - INSERT SQL " + sql);

			jdbcTemplate.update(sql,
					new Object[] { tenantCode, streamCode, virtualEntityCode, deploymentVersion, streamConfiguration.getStreamName(),
							streamConfiguration.getStreams().getStream().getVirtualEntityName(), streamConfiguration.getStreams().getStream().getVirtualEntityDescription(),
							streamConfiguration.getStreams().getStream().getVirtualEntityType(), streamConfiguration.getStreams().getStream().getVirtualEntityCategory(), System.currentTimeMillis(),
							streamConfiguration.getJsonMetadata(), streamConfiguration.getJSonSchema() });
		} else {
			String sql = "UPDATE STREAM_METADATA_CONFIGURATION  SET STREAM_NAME=?, VIRTUALENTITY_NAME=?, VIRTUALENTITY_DESCRIPTION=?, VIRTUALENTITY_TYPE=?, VIRTUALENTITY_CATEGORY=?, LASTUPDATE_TIMESTAMP=?, "
					+ "METADATA_JSON=?, SCHEMA_JSON=? WHERE TENANT_CODE=? AND STREAM_CODE=? AND VIRTUALENTITY_CODE=? AND DEPLOYMENT_VERSION=?";

			log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - UPDATE SQL " + sql);

			jdbcTemplate.update(sql,
					new Object[] { streamConfiguration.getStreamName(), streamConfiguration.getStreams().getStream().getVirtualEntityName(),
							streamConfiguration.getStreams().getStream().getVirtualEntityDescription(), streamConfiguration.getStreams().getStream().getVirtualEntityType(),
							streamConfiguration.getStreams().getStream().getVirtualEntityCategory(), System.currentTimeMillis(), streamConfiguration.getJsonMetadata(),
							streamConfiguration.getJSonSchema(), tenantCode, streamCode, virtualEntityCode, deploymentVersion });
		}

		log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - END");

	}

	public StreamMetadata findStreamMetadataConfigurationByPk(String tenantCode, String streamCode, String virtualentityCode, Long deploymentVersion) {

		log.debug("[StreamConfigurationDAO::findStreamMetadataConfigurationByPk] START - tenantCode: " + tenantCode + " | streamCode " + streamCode + " | virtualentityCode " + virtualentityCode
				+ " | deploymentVersion " + deploymentVersion);

		String sql = "SELECT TENANT_CODE, STREAM_CODE, VIRTUALENTITY_CODE, DEPLOYMENT_VERSION, STREAM_NAME, VIRTUALENTITY_NAME, VIRTUALENTITY_DESCRIPTION, VIRTUALENTITY_TYPE, "
				+ "VIRTUALENTITY_CATEGORY, LASTUPDATE_TIMESTAMP, METADATA_JSON, SCHEMA_JSON FROM STREAM_METADATA_CONFIGURATION "
				+ "WHERE TENANT_CODE=? AND STREAM_CODE=? AND  VIRTUALENTITY_CODE=? AND DEPLOYMENT_VERSION =?";

		log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - SQL " + sql);

		try {
			StreamMetadata streamMetadata = jdbcTemplate.queryForObject(sql, new Object[] { tenantCode, streamCode, virtualentityCode, deploymentVersion }, new StreamMetadataRowMapper());

			return streamMetadata;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			log.debug("[StreamConfigurationDAO::findStreamMetadataConfigurationByPk] END - tenantCode: " + tenantCode + " | streamCode " + streamCode + " | virtualentityCode " + virtualentityCode
					+ " | deploymentVersion " + deploymentVersion);
		}
	}

	public StreamMetadata findLastStreamMetadataConfiguration(String tenantCode, String streamCode, String virtualentityCode) {

		log.info("[StreamConfigurationDAO::findLastStreamMetadataConfiguration] START - tenantCode: " + tenantCode + " | streamCode " + streamCode + " | virtualentityCode " + virtualentityCode);

		String sql = "SELECT TENANT_CODE, STREAM_CODE, VIRTUALENTITY_CODE, DEPLOYMENT_VERSION, STREAM_NAME, VIRTUALENTITY_NAME, VIRTUALENTITY_DESCRIPTION, VIRTUALENTITY_TYPE, "
				+ "VIRTUALENTITY_CATEGORY, LASTUPDATE_TIMESTAMP, METADATA_JSON, SCHEMA_JSON FROM STREAM_METADATA_CONFIGURATION "
				+ "WHERE TENANT_CODE=? AND STREAM_CODE=? AND  VIRTUALENTITY_CODE=? ORDER BY DEPLOYMENT_VERSION DESC";

		log.debug("[StreamConfigurationDAO::findLastStreamMetadataConfiguration] - SQL " + sql);

		try {
			List<StreamMetadata> streamMetadata = jdbcTemplate.query(sql, new Object[] { tenantCode, streamCode, virtualentityCode }, new StreamMetadataRowMapper());
			if (streamMetadata == null || streamMetadata.size() == 0)
				return null;
			return streamMetadata.get(0);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			log.debug("[StreamConfigurationDAO::findLastStreamMetadataConfiguration] END - tenantCode: " + tenantCode + " | streamCode " + streamCode + " | virtualentityCode " + virtualentityCode);
		}
	}

}

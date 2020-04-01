/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.csi.yucca.gateway.service.dto.StreamMetadataDto.MetadataKey;
import org.csi.yucca.gateway.service.dto.StreamMetadataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;
@Service
public class StreamMetadataServiceImpl implements StreamMetadataService{

	private static final Logger log = Logger.getLogger("org.csi.yucca.gateway.service.StreamMetadataServiceImpl");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public StreamMetadataDto findOne(MetadataKey id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StreamMetadataDto> findAll(String gwId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StreamMetadataDto> findAll() {
		// TODO Auto-generated method stub
		log.debug("[StreamMetadataServiceImpl::findAll] START");
		
		String sql =  "SELECT "
							+ "TENANT_CODE, "
							+ " STREAM_CODE, "
							+ " VIRTUALENTITY_CODE, "
							+ " DEPLOYMENT_VERSION, "
							+ " STREAM_NAME, "
							+ " VIRTUALENTITY_NAME, "
							+ " VIRTUALENTITY_DESCRIPTION, "
							+ " VIRTUALENTITY_TYPE, "
							+ " VIRTUALENTITY_CATEGORY, "
							+ " LASTUPDATE_TIMESTAMP, "
							+ " METADATA_JSON, "
							+ " SCHEMA_JSON "
					+ "FROM     STREAM_METADATA_CONFIGURATION "
					+ "ORDER BY LASTUPDATE_TIMESTAMP DESC";

		log.debug("[StreamConfigurationDAO::saveStreamMetadataConfiguration] - SQL " + sql);
		System.out.println("=====> [StreamConfigurationDAO::saveStreamMetadataConfiguration] - SQL " + sql);

		try {
			List<StreamMetadataDto> streamMetadata = jdbcTemplate.query(sql, new Object[] { }, new RowMapper<StreamMetadataDto>() {

				@Override
				public StreamMetadataDto mapRow(ResultSet rs, int rowNum) throws SQLException {

					StreamMetadataDto sm = new StreamMetadataDto();
					
					sm.setTenantCode(rs.getString("TENANT_CODE"));
					sm.setStreamCode(rs.getString("STREAM_CODE"));
					sm.setVirtualEntityCode(rs.getString("VIRTUALENTITY_CODE"));
					sm.setDeploymentVersion(rs.getLong("DEPLOYMENT_VERSION"));
					sm.setStreamName(rs.getString("STREAM_NAME"));
					sm.setVirtualEntityName(rs.getString("VIRTUALENTITY_NAME"));
					sm.setVirtualEntityDescription(rs.getString("VIRTUALENTITY_DESCRIPTION"));
					sm.setVirtualEntityType(rs.getString("VIRTUALENTITY_TYPE"));
					sm.setVirtualEntityCategory(rs.getString("VIRTUALENTITY_CATEGORY"));
					sm.setLastUpdateTimestamp(rs.getLong("LASTUPDATE_TIMESTAMP"));
					sm.setMetadataJson(rs.getString("METADATA_JSON"));
					sm.setSchemaJson(rs.getString("SCHEMA_JSON"));
					
					return sm;
				}
			});

			return streamMetadata;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			log.debug("[StreamMetadataServiceImpl::findAll] END");
		}
	}

	@Override
	public void save(StreamMetadataDto dto) {
		// TODO Auto-generated method stub
		
	}
	
	
}

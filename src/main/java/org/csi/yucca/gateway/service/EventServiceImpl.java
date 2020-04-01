/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.gateway.service.dto.EventDto;

@Service
public class EventServiceImpl implements EventService{

	private static final Logger log = Logger.getLogger("org.csi.yucca.gateway.service.EventServiceImpl");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public EventDto findOne(String id) {
		log.debug("[EventServiceImpl::findOne] START");
		
		String sql =  "SELECT "
							+ " GW_ID, "
							+ " GW_INSERT_TIMESTAMP, "
							+ " GW_STATUS, "
							+ " SOURCE_CODE, "
							+ " STREAM_CODE, "
							+ " IS_APPLICATION, "
							+ " VALUES_JSON, "
							+ " LAST_ATTEMPT_ID, "
							+ " LAST_ATTEMPT_SEND_TIMESTAMP, "
							+ " LAST_ATTEMPT_RECEIVE_TIMESTAMP, "
							+ " LAST_RESPONSE, "
							+ " LAST_ENDPOINT, "
							+ " NUM_ATTEMPT "
					+ " FROM    EVENTS "
					+ " WHERE  EVENTS.GW_ID = ?"
					+ " ORDER BY LAST_ATTEMPT_SEND_TIMESTAMP DESC";

		log.debug("[EventServiceImpl::findOne] - SQL " + sql);
		System.out.println("=====> [EventServiceImpl::findOne] - SQL " + sql);

		try {
			EventDto event = jdbcTemplate.queryForObject(sql, new Object[] {id}, new RowMapper<EventDto>() {

				@Override
				public EventDto mapRow(ResultSet rs, int rowNum) throws SQLException {

					EventDto ev = new EventDto();
					
					ev.setEventId(rs.getString("GW_ID"));
					ev.setEventInsertTimestamp(rs.getLong("GW_INSERT_TIMESTAMP"));
					ev.setEventStatus(rs.getString("GW_STATUS"));
					ev.setEventSourceCode(rs.getString("SOURCE_CODE"));
					ev.setEventStreamCode(rs.getString("STREAM_CODE"));
					ev.setEventIsApp(rs.getBoolean("IS_APPLICATION"));
					ev.setEventJSON(rs.getString("VALUES_JSON"));
					ev.setEventLastAttempiID(rs.getString("LAST_ATTEMPT_ID"));
					ev.setEventLastAttemptSendTimestamp(rs.getLong("LAST_ATTEMPT_SEND_TIMESTAMP"));
					ev.setEventLastAttemptReceiveTimestamp(rs.getLong("LAST_ATTEMPT_RECEIVE_TIMESTAMP"));
					ev.setEventLastResponse(rs.getString("LAST_RESPONSE"));
					ev.setEventLastEndpoint(rs.getString("LAST_ENDPOINT"));
					ev.setEventNumAttempt(rs.getLong("NUM_ATTEMPT"));
					
					return ev;
				}
			});

			return event;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			log.debug("[EventServiceImpl::findOne] END");
		}
	}

	@Override
	public List<EventDto> findAll(String gwId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventDto> findAll() {
		log.debug("[EventServiceImpl::findAll] START");
		
		String sql =  "SELECT "
							+ " GW_ID, "
							+ " GW_INSERT_TIMESTAMP, "
							+ " GW_STATUS, "
							+ " SOURCE_CODE, "
							+ " STREAM_CODE, "
							+ " IS_APPLICATION, "
							+ " VALUES_JSON, "
							+ " LAST_ATTEMPT_ID, "
							+ " LAST_ATTEMPT_SEND_TIMESTAMP, "
							+ " LAST_ATTEMPT_RECEIVE_TIMESTAMP, "
							+ " LAST_RESPONSE, "
							+ " LAST_ENDPOINT, "
							+ " NUM_ATTEMPT "
					+ "FROM     EVENTS "
					+ "ORDER BY LAST_ATTEMPT_SEND_TIMESTAMP DESC";

		log.debug("[EventServiceImpl::findAll] - SQL " + sql);
		System.out.println("=====> [EventServiceImpl::findAll] - SQL " + sql);

		try {
			List<EventDto> events = jdbcTemplate.query(sql, new Object[] { }, new RowMapper<EventDto>() {

				@Override
				public EventDto mapRow(ResultSet rs, int rowNum) throws SQLException {

					EventDto ev = new EventDto();
					
					ev.setEventId(rs.getString("GW_ID"));
					ev.setEventInsertTimestamp(rs.getLong("GW_INSERT_TIMESTAMP"));
					ev.setEventStatus(rs.getString("GW_STATUS"));
					ev.setEventSourceCode(rs.getString("SOURCE_CODE"));
					ev.setEventStreamCode(rs.getString("STREAM_CODE"));
					ev.setEventIsApp(rs.getBoolean("IS_APPLICATION"));
					ev.setEventJSON(rs.getString("VALUES_JSON"));
					ev.setEventLastAttempiID(rs.getString("LAST_ATTEMPT_ID"));
					ev.setEventLastAttemptSendTimestamp(rs.getLong("LAST_ATTEMPT_SEND_TIMESTAMP"));
					ev.setEventLastAttemptReceiveTimestamp(rs.getLong("LAST_ATTEMPT_RECEIVE_TIMESTAMP"));
					ev.setEventLastResponse(rs.getString("LAST_RESPONSE"));
					ev.setEventLastEndpoint(rs.getString("LAST_ENDPOINT"));
					ev.setEventNumAttempt(rs.getLong("NUM_ATTEMPT"));
					
					return ev;
				}
			});

			return events;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			log.debug("[EventServiceImpl::findAll] END");
		}
	}

	@Override
	public void save(EventDto dto) {
		// TODO Auto-generated method stub
		
	}
	
	
}
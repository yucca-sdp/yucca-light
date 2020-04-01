/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service;

import java.util.List;

import org.csi.yucca.gateway.service.dto.EventDto;

public interface EventService {
	EventDto findOne(String id);
    
    List<EventDto> findAll(String gwId);

    List<EventDto> findAll();
    
    void save(EventDto dto);
}

/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service;

import java.util.List;

import org.csi.yucca.gateway.service.dto.AttemptDto;
import org.csi.yucca.gateway.service.dto.AttemptDto.AttemptKey;

public interface AttemptService {
	AttemptDto findOne(AttemptKey id);
    
    List<AttemptDto> findAll(String gwId);

    List<AttemptDto> findAll();
    
    void save(AttemptDto dto);
}

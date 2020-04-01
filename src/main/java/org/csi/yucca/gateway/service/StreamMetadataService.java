/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.service;

import java.util.List;

import org.csi.yucca.gateway.service.dto.StreamMetadataDto.MetadataKey;
import org.csi.yucca.gateway.service.dto.StreamMetadataDto;

public interface StreamMetadataService {
	StreamMetadataDto findOne(MetadataKey id);
    
    List<StreamMetadataDto> findAll(String gwId);

    List<StreamMetadataDto> findAll();
    
    void save(StreamMetadataDto dto);
}

/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.web;

import java.util.List;

import org.csi.yucca.gateway.service.StreamMetadataService;
import org.csi.yucca.gateway.service.dto.StreamMetadataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("request")
public class StreamMetadataController {

    @Autowired
    private StreamMetadataService streamMetadataService;
    
    @RequestMapping(value = "/streammetadata", method = RequestMethod.GET)
    public @ResponseBody List<StreamMetadataDto> eventsList() {
        return streamMetadataService.findAll();
    }

}

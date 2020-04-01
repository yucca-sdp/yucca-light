/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.csi.yucca.gateway.service.EventService;
import org.csi.yucca.gateway.service.dto.EventDto;

@Controller
@Scope("request")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private MessageSource ms;
   
    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public @ResponseBody List<EventDto> eventsList() {
        return eventService.findAll();
    }
}

 

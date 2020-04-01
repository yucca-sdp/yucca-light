/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.web;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;



@Controller
public class ConsoleController {

	@RequestMapping("/console")
	public String home(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model){
		model.addAttribute("name", name);
		return "index";
	}
	
	
}

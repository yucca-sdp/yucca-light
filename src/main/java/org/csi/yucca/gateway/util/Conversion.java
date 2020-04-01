/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;

import javax.xml.bind.DatatypeConverter;

import org.csi.yucca.gateway.api.ParseValidationUtil;
import org.csi.yucca.gateway.api.dto.Measure;
import org.csi.yucca.gateway.api.dto.StreamSensorEvent;
import org.csi.yucca.gateway.integration.dto.EventMessage;
import org.csi.yucca.gateway.integration.dto.MeasureWithRef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Conversion {

	public static EventMessage fromStreamSensorEvent2EventMessage(StreamSensorEvent eventDto) throws JsonProcessingException {
		if (eventDto!=null)
		{
			EventMessage eventMessage = new EventMessage();
			eventMessage.setSourceCode(eventDto.getSource());
			eventMessage.setStreamCode(eventDto.getStream());
			eventMessage.setMeasures(fromMeasureArray2MeasureWithRef(eventDto.getValues()));
			eventMessage.setApplication(eventDto.getApplication()!=null && eventDto.getApplication().length()>0);
			return eventMessage;
		}
		return null;
	}
	
	private static List<MeasureWithRef> fromMeasureArray2MeasureWithRef(Measure[] values) throws JsonProcessingException {
		List<MeasureWithRef> msrWithRef = new ArrayList<>();
		if (values != null)
		{
			ObjectMapper mapper = new ObjectMapper();
			for (int i = 0; i < values.length; i++) {
				msrWithRef.add(new MeasureWithRef(mapper.writeValueAsString(values[i])));
			}
			return msrWithRef;
		}	
		return null;
	}
	
	public static void addRefToMeasureWithRef(String ref, List<MeasureWithRef> measures)
	{
		if (measures!=null)
		{
			for (MeasureWithRef measureWithRef : measures) {
				measureWithRef.setRefId(ref);
			}
		}
	}

	public static Date fromStringToDate(String time) {
		Date o = null;
		SimpleDateFormat format = new SimpleDateFormat(ParseValidationUtil._msDateFormat);
		format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		o = format.parse(time, new ParsePosition(0));
		if (o == null) {
			// try older format with no ms
			format = new SimpleDateFormat(ParseValidationUtil._secDateFormat);
			format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
			o = format.parse(time, new ParsePosition(0));
			if (o == null) {
				// try timezone
				format = new SimpleDateFormat(ParseValidationUtil._msDateFormat_TZ);
				format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				o = format.parse(time, new ParsePosition(0));
				if (o == null) {
					// try older format timezone
					format = new SimpleDateFormat(ParseValidationUtil._secDateFormat_TZ);
					format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
					o = format.parse(time, new ParsePosition(0));
					 if (o == null) {
		                	// try isoDate with JAXB
		                	Calendar cal = DatatypeConverter.parseDateTime(time);
		                	o = cal.getTime();
		                }
				}
				
			}
		}
		return o;
	}



}

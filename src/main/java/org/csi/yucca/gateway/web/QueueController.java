/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.MessageId;
import org.csi.yucca.gateway.integration.dto.EventMessage;
import org.csi.yucca.gateway.integration.util.HeaderNameEnum;
import org.csi.yucca.gateway.service.AttemptService;
import org.csi.yucca.gateway.service.dto.AttemptDto;

@Controller
@Scope("request")
public class QueueController {

	@Autowired
	protected JmsTemplate jmsTemplate;

	@Autowired
	protected JmsTemplate jmsTemplateNoWait;

	@RequestMapping(value = "/queue", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<Message<EventMessage>>> queueMessageList(
			@RequestParam(value = "queueName", required = false, defaultValue = "*") String queueName) {

		Map<String, List<Message<EventMessage>>> events = jmsTemplate.browse("yucca_light." + queueName,
				new BrowserCallback<Map<String, List<Message<EventMessage>>>>() {
					@Override
					public Map<String, List<Message<EventMessage>>> doInJms(Session arg0, QueueBrowser queueBrowser)
							throws JMSException {
						Enumeration e = (Enumeration) queueBrowser.getEnumeration();

						Map<String, List<Message<EventMessage>>> events = new HashMap();
						while (e.hasMoreElements()) {
							ActiveMQObjectMessage o = (ActiveMQObjectMessage) e.nextElement();
							if (o != null) {
								if (events.containsKey(o.getDestination().getPhysicalName()))
									((List<Message<EventMessage>>) events.get(o.getDestination().getPhysicalName()))
											.add((Message<EventMessage>) o.getObject());
								else {
									events.put(o.getDestination().getPhysicalName(),
											new ArrayList<Message<EventMessage>>());
									((List<Message<EventMessage>>) events.get(o.getDestination().getPhysicalName()))
											.add((Message<EventMessage>) o.getObject());
								}
							}
						}
						return events;
					}

				});
		return events;

	}

	@RequestMapping(value = "/queue/move", method = RequestMethod.GET, params = {"queueSource", "queueDestination", "gwId"})
    public @ResponseBody boolean  queueMove(@RequestParam(value="queueSource", required=true) String queueSource,
    										@RequestParam(value="queueDestination", required=true) String queueDestination,
    										@RequestParam(value="gwId", required=true) String gwId) {

    	ActiveMQObjectMessage obj = jmsTemplate.browse("yucca_light."+queueSource,new MoveEventBrowserCallback(gwId,queueDestination));

    	if (obj==null)
    		return false;
    	
    	javax.jms.Message msd = jmsTemplate.receiveSelected("yucca_light."+queueSource, "JMSMessageID='"+obj.getJMSMessageID()+"'");
    	
    	if (msd==null)
    		return false;
    	try {
			jmsTemplate.send("yucca_light."+queueDestination, new CloneMessageCreator(msd));
		} catch (JmsException e) {
		}
    	return true;

    }

	@RequestMapping(value = "/queue/purge", method = RequestMethod.GET, params = "queueName")
	public @ResponseBody int deleteQueueMessage(String queueName) {
		int count = 0;
		try {

			while (jmsTemplateNoWait.receive("yucca_light."+queueName) != null) {
				count++;
			}
		} catch (JmsException e) {
			return count;
		}
		return count;
	}

	@RequestMapping(value = "/queue/summary", method = RequestMethod.GET, params = "queueName")
	public @ResponseBody Map<String, Map<String, Object>> queueMessageSummary(String queueName) {

		Map<String, Map<String, Object>> events = jmsTemplate.browse("yucca_light." + queueName,
				new BrowserCallback<Map<String, Map<String, Object>>>() {
					@Override
					public Map<String, Map<String, Object>> doInJms(Session arg0, QueueBrowser queueBrowser)
							throws JMSException {
						Enumeration e = (Enumeration) queueBrowser.getEnumeration();
						Map<String, Map<String, Object>> events = new HashMap();
						Map<String, Object> mappaInterna = null;
						while (e.hasMoreElements()) {
							ActiveMQObjectMessage o = (ActiveMQObjectMessage) e.nextElement();
							if (o != null) {
								if (events.containsKey(o.getDestination().getPhysicalName())) {
									mappaInterna = ((Map<String, Object>) events
											.get(o.getDestination().getPhysicalName()));
									mappaInterna.put("countMessages",
											((Integer) mappaInterna.get("countMessages")) + 1);
									mappaInterna.put("lastmessage", o.getObject());
								} else {
									events.put(o.getDestination().getPhysicalName(), new HashMap<String, Object>());
									mappaInterna = ((Map<String, Object>) events
											.get(o.getDestination().getPhysicalName()));
									mappaInterna.put("countMessages", new Integer(1));
									mappaInterna.put("lastmessage", o.getObject());
								}
							}
						}

						return events;
					}

				});
		return events;

	}

	private class MoveEventBrowserCallback implements BrowserCallback<ActiveMQObjectMessage> {
		String gwId, destinationName;

		public MoveEventBrowserCallback(String gwId, String destinationName) {
			this.gwId = gwId;
			this.destinationName = destinationName;
		}

		public ActiveMQObjectMessage doInJms(Session session, QueueBrowser queueBrowser) throws JMSException {
			Enumeration e = (Enumeration) queueBrowser.getEnumeration();

			Map<String, List<Message<EventMessage>>> events = new HashMap();
			while (e.hasMoreElements()) {
				ActiveMQObjectMessage o = (ActiveMQObjectMessage) e.nextElement();
				if (o != null) {
					Message<EventMessage> msg = (Message<EventMessage>) o.getObject();

					if (msg.getHeaders().get(HeaderNameEnum.gwId.name()) != null
							&& msg.getHeaders().get(HeaderNameEnum.gwId.name()).toString().equals(gwId)) {
						return o;
					}
				}
			}
			return null;
		}
		
		
		
	}
    private class CloneMessageCreator implements MessageCreator{

    	javax.jms.Message msg;
    	
    	public  CloneMessageCreator(javax.jms.Message msg) {
    			this.msg = msg;
		}
		@Override
		public javax.jms.Message createMessage(Session session) throws JMSException {
			
			return msg;
		}
    	
    }

}

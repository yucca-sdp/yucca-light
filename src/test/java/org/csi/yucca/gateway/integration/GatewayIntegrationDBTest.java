/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import java.net.URISyntaxException;
import java.util.Arrays;

import org.csi.yucca.gateway.integration.dto.EventMessage;
import org.csi.yucca.gateway.integration.dto.MeasureWithRef;
import org.csi.yucca.gateway.util.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

public class GatewayIntegrationDBTest extends AbstractIntegrationTest{
	
	@Autowired
	private YuccaLikeService yuccaLikeService;
	
	@Autowired
    protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	protected JmsTemplate jmsTemplate;
	
		
	@Test
	 public void testSendValidMessage() throws URISyntaxException, InterruptedException {
		
		setMockYuccaRTServiceServer();
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
							requestTo("https://yucca-stream/api/input/"+codTenant)).
						andRespond(MockRestResponseCreators.withSuccess());
		
		EventMessage msg =  new EventMessage();
		msg.setApplication(false);
		msg.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg.setStreamCode("temperature");
		msg.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"12.10\" }  }")}));

		int numeroMessaggi = JdbcTestUtils.countRowsInTable(jdbcTemplate, "EVENTS");
		
		yuccaLikeService.sendEventToYucca(msg);
		Thread.sleep(2000);

		int numeroMessaggiDopo = JdbcTestUtils.countRowsInTable(jdbcTemplate, "EVENTS");

		Assert.assertEquals(numeroMessaggi+1, numeroMessaggiDopo);
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();
    }

	@Test
	 public void testSend503ValidMessage() throws URISyntaxException, InterruptedException {
		
		setMockYuccaRTServiceServer();
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
							requestTo("https://yucca-stream/api/input/"+codTenant)).
						andRespond(MockRestResponseCreators.withServerError());
		
		EventMessage msg =  new EventMessage();
		msg.setApplication(false);
		msg.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg.setStreamCode("temperature");
		msg.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"12.ss10\" }  }")}));

		int numeroMessaggi = JdbcTestUtils.countRowsInTable(jdbcTemplate, "EVENTS");
		
		yuccaLikeService.sendEventToYucca(msg);
		Thread.sleep(2000);
		
		int numeroMessaggiDopo = JdbcTestUtils.countRowsInTable(jdbcTemplate, "EVENTS");

		Assert.assertEquals(numeroMessaggi+1, numeroMessaggiDopo);
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();

   }
	
}

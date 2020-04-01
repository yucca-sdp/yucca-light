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
import org.csi.yucca.gateway.integration.util.IntegrationTestUtils;
import org.csi.yucca.gateway.util.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

public class GatewayIntegrationJmsTest extends AbstractIntegrationTest{
	
	@Autowired
	private YuccaLikeService yuccaLikeService;
	
	
	@Autowired
	protected JmsTemplate jmsTemplate;
	
		
	@Test
	 public void testSendValidMessage() throws URISyntaxException, InterruptedException, AssertionError {
		
		setMockYuccaRTServiceServer();
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
							requestTo("https://yucca-stream/api/input/"+codTenant)).
//						andExpect(MockRestRequestMatchers.header("Authorization", "Basic dW5pdFRlbmFudDp1bml0VGVuYW50UHdk")).
						andRespond(MockRestResponseCreators.withSuccess());


		EventMessage msg =  new EventMessage();
		msg.setApplication(false);
		msg.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg.setStreamCode("temperature");
		msg.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"12.10\" }  }")}));

		int numeroMessaggiCoda =  IntegrationTestUtils.countQueueElement("yucca_light.sent_rt", jmsTemplate);
		
		yuccaLikeService.sendEventToYucca(msg);
		Thread.sleep(2000);

		int numeroMessaggiCodaDopo =  IntegrationTestUtils.countQueueElement("yucca_light.sent_rt", jmsTemplate);

		Assert.assertEquals(numeroMessaggiCoda+1, numeroMessaggiCodaDopo);
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
		msg.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"12.sss10\" }  }")}));

		int numeroMessaggiCoda =  IntegrationTestUtils.countQueueElement("yucca_light.sent_invalid", jmsTemplate);
		
		yuccaLikeService.sendEventToYucca(msg);
		Thread.sleep(2000);
		
		int numeroMessaggiCodaDopo =  IntegrationTestUtils.countQueueElement("yucca_light.sent_invalid", jmsTemplate);

		Assert.assertEquals(numeroMessaggiCoda+1, numeroMessaggiCodaDopo);
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();

   }
	
}

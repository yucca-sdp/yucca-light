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
import org.springframework.boot.test.IntegrationTest;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;


@IntegrationTest({"server.port=9000"})
public class GatewayIntegrationA2ATest extends AbstractIntegrationTest{
	
	
	@Autowired
	private YuccaLikeService yuccaLikeService;
	
	@Autowired
    protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	protected JmsTemplate jmsTemplate;
	

	@Test
	 public void testSendValidMessage() throws URISyntaxException, InterruptedException {

		
		EventMessage msg =  new EventMessage();
		msg.setApplication(false);
		msg.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg.setStreamCode("temperature");
		msg.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"1\" }  }")}));

		EventMessage msg1 =  new EventMessage();
		msg1.setApplication(false);
		msg1.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg1.setStreamCode("temperature");
		msg1.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"2\" }  }")}));

		EventMessage msg2 =  new EventMessage();
		msg2.setApplication(false);
		msg2.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg2.setStreamCode("temperature");
		msg2.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"3\" }  }")}));

		EventMessage msg3 =  new EventMessage();
		msg3.setApplication(false);
		msg3.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg3.setStreamCode("temperature");
		msg3.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"4\" }  }")}));

		int numeroTentativi = JdbcTestUtils.countRowsInTable(jdbcTemplate, "ATTEMPT_HISTORY");
		
		
		int numeroMessaggiCoda =  IntegrationTestUtils.countQueueElement("yucca_light.sent_a2a", jmsTemplate);

		setMockYuccaA2AServiceServer();

		mockYuccaA2AServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-api/stream/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		mockYuccaA2AServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-api/stream/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		
		yuccaLikeService.sendEventToYucca(msg);
		yuccaLikeService.sendEventToYucca(msg1);
		yuccaLikeService.sendEventToYucca(msg2);
		yuccaLikeService.sendEventToYucca(msg3);

		// verify only 2 calls (3 + 1)


		Thread.sleep(10000);

		int numeroMessaggiCodaDopo =  IntegrationTestUtils.countQueueElement("yucca_light.sent_a2a", jmsTemplate);

		int numeroTentativiDopo = JdbcTestUtils.countRowsInTable(jdbcTemplate, "ATTEMPT_HISTORY");

		IntegrationTestUtils.logQueueLastElement("yucca_light.sent_a2a", jmsTemplate);
		
// two group
		Assert.assertEquals(numeroMessaggiCoda+4, numeroMessaggiCodaDopo);
		Assert.assertEquals(numeroTentativi+8, numeroTentativiDopo);
		
		mockYuccaA2AServiceServer.verify();
    }
	
	@Test
	 public void testTwoStreamSendValidMessage() throws URISyntaxException, InterruptedException {
		
		setMockYuccaA2AServiceServer();

		EventMessage msg1 =  new EventMessage();
		msg1.setApplication(false);
		msg1.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg1.setStreamCode("temperature");
		msg1.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"2\" }  }")}));

		EventMessage msg2 =  new EventMessage();
		msg2.setApplication(false);
		msg2.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg2.setStreamCode("temperature");
		msg2.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"3\" }  }")}));

		EventMessage msg3 =  new EventMessage();
		msg3.setApplication(false);
		msg3.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
		msg3.setStreamCode("umidity");
		msg3.setMeasures(Arrays.asList(new MeasureWithRef[]{new MeasureWithRef("{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"4\" }  }")}));
		
		int numeroMessaggiCoda =  IntegrationTestUtils.countQueueElement("yucca_light.sent_a2a", jmsTemplate);
		int numeroTentativi = JdbcTestUtils.countRowsInTable(jdbcTemplate, "ATTEMPT_HISTORY");

		yuccaLikeService.sendEventToYucca(msg3);
		yuccaLikeService.sendEventToYucca(msg1);
		yuccaLikeService.sendEventToYucca(msg2);

		
		// verify only 2 calls (1 + 1)
		mockYuccaA2AServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-api/stream/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());
		mockYuccaA2AServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-api/stream/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());
		
		Thread.sleep(10000);
		
//		IntegrationTestUtils.logQueueLastElement("yucca_light.sent_a2a", jmsTemplate);
		int numeroMessaggiCodaDopo =  IntegrationTestUtils.countQueueElement("yucca_light.sent_a2a", jmsTemplate);
		int numeroTentativiDopo = JdbcTestUtils.countRowsInTable(jdbcTemplate, "ATTEMPT_HISTORY");
		
//two group
		Assert.assertEquals(numeroMessaggiCoda+3, numeroMessaggiCodaDopo);
		Assert.assertEquals(numeroTentativi+6, numeroTentativiDopo);
//		IntegrationTestUtils.logQueueLastElement("yucca_light.sent_a2a", jmsTemplate);
		mockYuccaA2AServiceServer.verify();
}


//	@Test
//	 public void testSend503ValidMessage() throws URISyntaxException, InterruptedException {
//		
//		boolean received = false;
//		
//		EventMessage msg =  new EventMessage();
//		msg.setApplication(false);
//		msg.setSourceCode("550e8400-e29b-41d4-a716-446655440000");
//		msg.setStreamCode("temperature");
//		msg.setMeasures("[{\"time\": \"2014-05-13T17:08:52.00+02:00\", \"components\": {\"c0\": \"12.sss10\" }  } ]");
//
//		int numeroMessaggi = JdbcTestUtils.countRowsInTable(jdbcTemplate, "EVENTS");
//		
//		yuccaLikeService.sendEventToYucca(msg);
//		Thread.sleep(2000);
//		
//		int numeroMessaggiDopo = JdbcTestUtils.countRowsInTable(jdbcTemplate, "EVENTS");
//
//		Assert.assertEquals(numeroMessaggi+1, numeroMessaggiDopo);
//   }
//	
}

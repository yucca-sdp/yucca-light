/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.csi.yucca.gateway.YuccaLightApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("unit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = YuccaLightApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=9000",
	})
public class AbstractIntegrationTest {

	@Value("${yucca.tenant.code}")
	public String codTenant;
	
	@Autowired
	private RestTemplate httpRestRTTemplate;
	
	@Autowired
	private RestTemplate httpRestA2ATemplate;

	@Autowired
	private RestTemplate metadataRestTemplate;

	public MockRestServiceServer mockYuccaRTServiceServer; 

	public MockRestServiceServer mockYuccaA2AServiceServer; 

	public MockRestServiceServer mockYuccaMetadataServiceServer; 

	private ClientHttpRequestFactory originalYuccaRTRequestFactory;

	private ClientHttpRequestFactory originalYuccaA2ARequestFactory;

	private ClientHttpRequestFactory originalYuccaMetadataRequestFactory;

	

	@Before
	public void setUp() throws Exception {
		originalYuccaRTRequestFactory = httpRestRTTemplate.getRequestFactory();
		originalYuccaA2ARequestFactory = httpRestA2ATemplate.getRequestFactory();
		originalYuccaMetadataRequestFactory = metadataRestTemplate.getRequestFactory();
    }

	public void setMockYuccaRTServiceServer()
	{
		mockYuccaRTServiceServer = MockRestServiceServer.createServer(httpRestRTTemplate);
	}

	public void setMockYuccaA2AServiceServer()
	{
		mockYuccaA2AServiceServer = MockRestServiceServer.createServer(httpRestA2ATemplate);
	}

	public void removeMockYuccaRTServiceServer()
	{
		httpRestRTTemplate.setRequestFactory(originalYuccaRTRequestFactory);
	}

	public void removeMockYuccaA2AServiceServer()
	{
		httpRestA2ATemplate.setRequestFactory(originalYuccaA2ARequestFactory);
	}

	public void setMockYuccaMetadataServiceServer()
	{
		mockYuccaMetadataServiceServer = MockRestServiceServer.createServer(metadataRestTemplate);
	}


	public void removeMockYuccaMetadataServiceServer()
	{
		metadataRestTemplate.setRequestFactory(originalYuccaMetadataRequestFactory);
	}

	private static final Logger LOGGER = Logger.getLogger(AbstractIntegrationTest.class);
	
}

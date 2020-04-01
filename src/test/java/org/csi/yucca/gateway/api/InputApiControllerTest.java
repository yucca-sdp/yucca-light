/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.URISyntaxException;

import org.csi.yucca.gateway.util.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class InputApiControllerTest extends AbstractIntegrationTest {

	private RestTemplate restTemplate = new TestRestTemplate("sandbox","sandbox$1");

	// TODO: Validare uno stream con tutte le componenti, inclusi datetime e longit/latitud 
	
	
    @Test
    public void testCredentialInvalid() throws URISyntaxException {
    	restTemplate = new TestRestTemplate("sandbox","saggndbox$1");
    	
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> json = new RequestEntity<String>("{ a }",headers,
    												HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));
    	
    	ResponseEntity<String> result;
		try {
			result = restTemplate.exchange(json,String.class);
			Assert.assertTrue(result.getStatusCode().is4xxClientError());
		} catch (RestClientException e) {
			Assert.assertTrue(true);
			Assert.assertTrue(e.getCause() instanceof HttpRetryException);
			Assert.assertTrue(((HttpRetryException)e.getCause()).responseCode() == 401);
			return;
		}

    }

	 
	 @Test
    public void testJsonInvalid() throws URISyntaxException {
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> invalidJson = new RequestEntity<String>("{ a }",headers,
    												HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));
    	
    	ResponseEntity<String> result = restTemplate.exchange(invalidJson,String.class);

    	Assert.assertTrue(result.getStatusCode().is5xxServerError());
    	Assert.assertTrue(result.getBody().toString().contains("E012"));

    }
	 
	@Test
    public void testJsonValidWithNoSensorOrStream() throws URISyntaxException {
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValidButDifferent = new RequestEntity<String>("{ \"a\":\"a\" }",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValidButDifferent,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());
		Assert.assertTrue(result.getBody().toString().contains("E011"));
    	
    	
    	
    }
	@Test
    public void testJsonValidWithDifferentComponents() throws URISyntaxException {
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValidButDiffComponent = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c00000\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValidButDiffComponent,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());
		Assert.assertTrue(result.getBody().toString().contains("E013"));
    	
    	
    }
	
	@Test
    public void testJsonValidWithoutSchema() throws URISyntaxException {
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValidButDiffComponent = new RequestEntity<String>("{"
    			+ "  \"stream\":\"ss\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c00000\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValidButDiffComponent,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());
		Assert.assertTrue(result.getBody().toString().contains("E011"));
    	
    	
    }
	@Test
    public void testJsonValid() throws URISyntaxException {
		setMockYuccaRTServiceServer();
		
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());
		
		MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode()== HttpStatus.ACCEPTED);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();
    	
    }
	
	@Test
    public void testJsonValidWithStringLong() throws URISyntaxException {
		setMockYuccaRTServiceServer();
		
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());
		
		MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c0\": \"17.4\" }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode()== HttpStatus.ACCEPTED);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();
    	
    }
	@Test
    public void testJsonDifferentTimeformat() throws URISyntaxException {
		setMockYuccaRTServiceServer();
		
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
    	
    	jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58+0200\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
    	
    	jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58.000+0200\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
    	
		jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58.000+02:00\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());

		jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58.000Z\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());

		jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-ss05-13T17:08:58.000Z\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());

		jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": ["
    			+ "{\"time\": \"2014-05-13T17:08:58.000Z\",\"components\": { \"c0\": 17.4 }},"
    			+ "{\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c0\": 17.4 }},"
    			+ "{\"time\": \"2014-05-1ss3T17:08:58+0200\",\"components\": { \"c0\": 17.4 }},"
    			+ "{\"time\": \"2014-05-13T17:08:58.000+02:00\",\"components\": { \"c0\": 17.4 }}"
    			+ "]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());

		jsonValid = new RequestEntity<String>("{"
    			+ "  \"stream\":\"temperature\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": ["
    			+ "{\"time\": \"2014-05-13T17:08:58.000Z\",\"components\": { \"c0\": 17.4 }},"
    			+ "{\"time\": \"2014-05-13T17:08:58Z\",\"components\": { \"c0\": 17.4 }},"
    			+ "{\"time\": \"2014-05-13T17:08:58+0200\",\"components\": { \"c0\": 17.4 }},"
    			+ "{\"time\": \"2014-05-13T17:08:58.000+02:00\",\"components\": { \"c0\": 17.4 }}"
    			+ "]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(jsonValid,String.class);
		
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();
	}
	
	@Test
    public void testJsonNoStream() throws URISyntaxException {
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> json = new RequestEntity<String>("{"
    			+ "  \"stream\":\"\",\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05s-13T17:08:58Z\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(json,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());
		Assert.assertTrue(result.getBody().toString().contains("E017"));
    	
    	json = new RequestEntity<String>("{"
    			+ "  \"stream\":null,\"sensor\": \"550e8400-e29b-41d4-a716-446655440000\",\"values\": [{"
  		+ "\"time\": \"2014-05s-13T17:08:58Z\",\"components\": { \"c0\": 17.4 }}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	result = restTemplate.exchange(json,String.class);
		
		Assert.assertTrue(result.getStatusCode().is5xxServerError());
		Assert.assertTrue(result.getBody().toString().contains("E011"));
    	
    }
	
	@Test
    public void testJsonStreamAllDataType() throws URISyntaxException {
		setMockYuccaRTServiceServer();
		mockYuccaRTServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-stream/api/input/"+codTenant)).
			andRespond(MockRestResponseCreators.withSuccess());

		
    	MultiValueMap<String, String> headers = new HttpHeaders();
    	headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
    	headers.add(HttpHeaders.ACCEPT, "application/json");
    	
    	RequestEntity<String> jsonValidButDiffComponent = new RequestEntity<String>("{"
    			+ "  \"stream\":\"aleStreamTuttiTipiDa\",\"sensor\": \"c4b5f767-1de7-4d49-9d48-06602c704469\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": {"
  		+ "\"Intero\":11,\"Long\":12,\"Double\":1.2,\"Float\":1.3,\"Stringa\":\"ciao\",\"Booleano\":true,"
  		+ "\"Data\":\"2014-05-13T17:08:58Z\",\"Longitudine\":22.22,\"Latitudine\":33.33}"
  		+ "}]}",headers,
				HttpMethod.POST,new URI("http://localhost:9000/yucca-light/api/input/"+codTenant));

    	ResponseEntity<String> result = restTemplate.exchange(jsonValidButDiffComponent,String.class);
		
		Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mockYuccaRTServiceServer.verify();
		removeMockYuccaRTServiceServer();

    }
	
	
	@Test
	public void tttt(){
		String jsonSchema  ="{\"title\": \"aleStreamTuttiTipiDa sensor schema\",\"type\": \"object\","
				+ "\"properties\": {\"stream\": {\"type\": \"string\"},\"sensor\": {\"type\": \"string\"},"
				+ "\"values\": {\"type\": \"array\",\"minItems\" : 1,\"items\": { \"type\": \"object\","
				+ "\"properties\": {\"time\": {\"type\": \"string\"},"
				+ "\"components\" : {\"type\": \"object\",\"properties\": {"
				+ "\"Intero\" : {\"type\" : [\"integer\" , \"string\"]},"
				+ "\"Long\" : {\"type\" : \"number\"},"
				+ "\"Double\" : {\"type\" : \"number\"},"
				+ "\"Float\" : {\"type\" : \"number\"},"
				+ "\"Stringa\" : {\"type\" : \"string\"},"
				+ "\"Booleano\" : {\"type\" : \"boolean\"},"
				+ "\"Data\" : {\"type\" : \"string\",  \"format\": \"date-time\"},"
				+ "\"Longitudine\" : {\"type\" : \"number\"},"
				+ "\"Latitudine\" : {\"type\" : \"number\"}},"
				+ "\"required\": [\"Intero\",\"Long\",\"Double\",\"Float\",\"Stringa\",\"Booleano\",\"Data\",\"Longitudine\",\"Latitudine\"]}}}}},"
				+ "\"required\": [\"stream\", \"sensor\", \"values\"]}";
		String json = "{"
    			+ "  \"stream\":\"aleStreamTuttiTipiDa\",\"sensor\": \"c4b5f767-1de7-4d49-9d48-06602c704469\",\"values\": [{"
  		+ "\"time\": \"2014-05-13T17:08:58Z\",\"components\": {"
  		+ "\"Intero\":\"11\",\"Long\":12,\"Double\":1.2,\"Float\":1.3,\"Stringa\":\"ciao\",\"Booleano\":true,"
  		+ "\"Data\":\"2014-05-13T17:08:58+01:00\",\"Longitudine\":22.22,\"Latitudine\":33.33}"
  		+ "}]}";
		
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		final JsonSchema schema;
		ProcessingReport pr;
		
		
		try {
			JsonNode schemaNode = JsonLoader.fromString(jsonSchema);
			JsonNode jsonNode = JsonLoader.fromString(json);

			schema = factory.getJsonSchema(schemaNode);
			pr = schema.validate(jsonNode);
			while (pr.iterator().hasNext()) {
				ProcessingMessage message = (ProcessingMessage) pr.iterator().next();
			    System.out.println("Message" + message.asJson().get("message").asText());
				
			}
			System.out.println("pr: " + pr.isSuccess());
		} catch (IOException | ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}

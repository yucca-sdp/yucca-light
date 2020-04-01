/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration;

import org.csi.yucca.gateway.util.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;


public class LoadConfigurationTest extends AbstractIntegrationTest{

	@Autowired
	private StreamConfigurationManager streamConfigurationManager;
	
	
	public void setStreamConfigurationManager(StreamConfigurationManager streamConfigurationManager) {
		this.streamConfigurationManager = streamConfigurationManager;
	}

	@Test
	public void testFirstTime() {
		setMockYuccaMetadataServiceServer();
		mockYuccaMetadataServiceServer.expect(MockRestRequestMatchers.
				requestTo("https://yucca-metadatamanagement/management/stream/metadata?tenant=unitTenant&consumerType=yuccaLight")).
				andRespond(MockRestResponseCreators.withSuccess("[{\"streamCode\":\"aleStreamTuttiTipiDa\",\"streamName\":\"aleStreamNameTuttiTipiDato\",\"configData\":{\"tenantCode\":\"unitTenant\"},\"streams\":{\"stream\":{\"virtualEntityName\":\"SmartGeneralSensor\",\"virtualEntityDescription\":\"SmartGeneralSensor\",\"virtualEntityCode\":\"c4b5f767-1de7-4d49-9d48-06602c704469\",\"virtualEntityType\":\"Device\",\"virtualEntityCategory\":\"Smart\",\"domainStream\":\"ENVIRONMENT\",\"visibility\":\"public\",\"deploymentVersion\":1,\"streamIcon\":\"img/stream-icon-default.png\",\"components\":{\"element\":[{\"componentName\":\"Intero\",\"componentAlias\":\"Intero\",\"tolerance\":12.0,\"measureUnit\":\"-\",\"measureUnitCategory\":\"other\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"int\"},{\"componentName\":\"Long\",\"componentAlias\":\"Long\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"long\"},{\"componentName\":\"Double\",\"componentAlias\":\"Double\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"double\"},{\"componentName\":\"Float\",\"componentAlias\":\"Float\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"float\"},{\"componentName\":\"Stringa\",\"componentAlias\":\"Stringa\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"string\"},{\"componentName\":\"Booleano\",\"componentAlias\":\"Booleano\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"boolean\"},{\"componentName\":\"Data\",\"componentAlias\":\"Data\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"dateTime\"},{\"componentName\":\"Longitudine\",\"componentAlias\":\"Longitudine\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"longitude\"},{\"componentName\":\"Latitudine\",\"componentAlias\":\"Latitudine\",\"tolerance\":12.0,\"measureUnit\":\"m\",\"measureUnitCategory\":\"length\",\"phenomenon\":\"-\",\"phenomenonCategory\":\"other\",\"dataType\":\"longitude\"}]},\"streamTags\":{\"tags\":[{\"tagCode\":\"CARBON\"}]}}}},{\"streamCode\":\"temperature\",\"streamName\":\"Temperatura rilevata\",\"configData\":{\"tenantCode\":\"unitTenant\"},\"streams\":{\"stream\":{\"virtualEntityName\":\"ArduinoReference\",\"virtualEntityDescription\":\"Arduino Reference\",\"virtualEntityCode\":\"550e8400-e29b-41d4-a716-446655440000\",\"virtualEntityType\":\"Device\",\"virtualEntityCategory\":\"Smart\",\"domainStream\":\"ENVIRONMENT\",\"licence\":\"CC BY 4.0\",\"visibility\":\"public\",\"deploymentVersion\":1,\"components\":{\"element\":[{\"componentName\":\"c0\",\"componentAlias\":\"c0\",\"tolerance\":0.0,\"measureUnit\":\"C\",\"measureUnitCategory\":\"temperature\",\"phenomenon\":\"air temperature\",\"phenomenonCategory\":\"environment\",\"dataType\":\"float\"}]},\"streamTags\":{\"tags\":[{\"tagCode\":\"AIR\"}]},\"virtualEntityPositions\":{\"position\":[{\"lon\":7.6681804,\"lat\":45.0396001,\"elevation\":0.0,\"floor\":0,\"room\":\"0\"}]}}}}]", MediaType.APPLICATION_JSON));
		
		streamConfigurationManager.loadConfiguration();
		
		mockYuccaMetadataServiceServer.verify();
		removeMockYuccaMetadataServiceServer();
	}

}

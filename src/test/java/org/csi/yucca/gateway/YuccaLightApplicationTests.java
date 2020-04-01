/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway;

import org.csi.yucca.gateway.api.InputApiControllerTest;
import org.csi.yucca.gateway.configuration.LoadConfigurationTest;
import org.csi.yucca.gateway.configuration.RefreshConfigurationTest;
import org.csi.yucca.gateway.integration.GatewayIntegrationA2ATest;
import org.csi.yucca.gateway.integration.GatewayIntegrationDBTest;
import org.csi.yucca.gateway.integration.GatewayIntegrationJmsTest;
import org.csi.yucca.gateway.integration.GatewayIntegrationSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(Suite.class)
@SuiteClasses({ LoadConfigurationTest.class,
				RefreshConfigurationTest.class,
				GatewayIntegrationSuite.class,
				InputApiControllerTest.class
})
public class YuccaLightApplicationTests {

}

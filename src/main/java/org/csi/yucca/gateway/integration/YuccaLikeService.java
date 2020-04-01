/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import org.csi.yucca.gateway.integration.dto.EventMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(name="yuccaLikeGateway")
public interface YuccaLikeService {

	@Gateway(headers={@GatewayHeader(name="errorChannel", value="outputFailureYuccaChannel"),
					  @GatewayHeader(name="gwStatus", value="TO_SEND_RT")
					 },
			requestChannel="yuccaLikeRtChannel", replyTimeout=20)
	public void sendEventToYucca(EventMessage message);

	
}

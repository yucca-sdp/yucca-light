/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.util;

public enum EventStateEnum {

	TO_SEND_RT, 
	SENDING_RT_PROGRESS,
	SENDING_FAILED,
	SENT_RT,
	TO_SEND_A2A, 
	SENDING_A2A_PROGRESS,
	SENT_A2A, 
	SENT_INVALID
}

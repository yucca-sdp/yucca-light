/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.integration;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.integration.handler.advice.AbstractRequestHandlerAdvice;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

public class PreserveMessageRequestHTTPHandlerAdvice extends AbstractRequestHandlerAdvice {
	private static final Logger log=Logger.getLogger(PreserveMessageRequestHTTPHandlerAdvice.class);


	private volatile MessageChannel outputChannel;

	private String statusHeaderName="statusHeaderName";
	private String httpCodeHeaderName="httpCodeHeaderName";
	private String responseDetailHeaderName="responseDetailHeaderName";


	private String statusValueException="SENDING_FAILED";
	private Map<String, String> statusValueMap = null;
	

	private final MessagingTemplate messagingTemplate = new MessagingTemplate();

	private volatile EvaluationContext evaluationContext;

	public void setOutputChannel(MessageChannel outputChannel) {
		Assert.notNull(outputChannel,"'successChannel' must not be null");
		this.outputChannel = outputChannel;
	}


	@Override
	protected void onInit() throws Exception {
		super.onInit();
		if (this.getBeanFactory() != null) {
			this.messagingTemplate.setBeanFactory(this.getBeanFactory());
		}
	}

	@Override
	protected Object doInvoke(ExecutionCallback callback, Object target, Message<?> message) throws Exception {
		try {
			Object result = callback.execute();
			
			if (this.outputChannel != null) {
				this.evaluateHTTPStatusCodeSuccessExpression(message,result);
			}
			return result;
		}
		catch (Exception e) {
			Exception actualException = this.unwrapExceptionIfNecessary(e);

			if (this.outputChannel != null) {
				this.evaluateFailureExpression(message, actualException);
			}
			return null;
		}
	}

	private void evaluateHTTPStatusCodeSuccessExpression(Message<?> message, Object result) throws Exception {
			log.info("response:"+result);
			String httpCodeHeaderValue = null;
			String responseDetailHeaderValue = null;
			GenericMessage msg = (GenericMessage) result;
			HttpStatus status = (HttpStatus) msg.getHeaders().get("http_statusCode");
			httpCodeHeaderValue = status.toString();
			String statusHeaderValue = statusValueMap.get(status.series().name());
			if (msg.getPayload() instanceof String)
			{
				responseDetailHeaderValue = msg.getPayload().toString();
			}
			else if (msg.getPayload() instanceof ResponseEntity) 
			{
				if (((ResponseEntity)msg.getPayload()).hasBody())  {
					responseDetailHeaderValue = ((ResponseEntity)msg.getPayload()).getBody().toString();
				}
			}

			this.messagingTemplate.send(this.outputChannel, getMessageBuilderFactory().fromMessage(message).
					setHeader(responseDetailHeaderName, responseDetailHeaderValue).setHeader(httpCodeHeaderName, httpCodeHeaderValue).
					setHeader(statusHeaderName, statusHeaderValue).build());
	}

	private void evaluateFailureExpression(Message<?> message, Exception exception) throws Exception {
		log.info("exception:"+exception.getMessage());
		this.messagingTemplate.send(this.outputChannel, getMessageBuilderFactory().fromMessage(message).
				setHeader(responseDetailHeaderName, exception.getMessage()).setHeader(httpCodeHeaderName, 999).
				setHeader(statusHeaderName, statusValueException).build());
	}

	protected StandardEvaluationContext createEvaluationContext(){
		return ExpressionUtils.createStandardEvaluationContext(this.getBeanFactory());
	}


	public void setStatusHeaderName(String statusHeaderName) {
		this.statusHeaderName = statusHeaderName;
	}


	public void setHttpCodeHeaderName(String httpCodeHeaderName) {
		this.httpCodeHeaderName = httpCodeHeaderName;
	}


	public void setResponseDetailHeaderName(String responseDetailHeaderName) {
		this.responseDetailHeaderName = responseDetailHeaderName;
	}


	public void setStatusValueException(String statusValueException) {
		this.statusValueException = statusValueException;
	}


	public void setStatusValueMap(Map<String, String> statusValueMap) {
		this.statusValueMap = statusValueMap;
	}


}
/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.util;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Convenient subclass of {@link RestTemplate} cloned by TestRestTemplate .
 * They are fault tolerant, and optionally can carry Basic authentication headers. If
 * Apache Http Client 4.3.2 or better is available (recommended) it will be used as the
 * client, and by default configured to ignore cookies and redirects.
 *
 */
public class YuccaRestTemplate extends RestTemplate {

	/**
	 * Create a new {@link TestRestTemplate} instance.
	 * @param httpClientOptions client options to use if the Apache HTTP Client is used
	 */
	public YuccaRestTemplate(HttpClientOption... httpClientOptions) {
		this(null, null, 30000, httpClientOptions);
	}

	/**
	 * Create a new {@link TestRestTemplate} instance with the specified credentials.
	 * @param username the username to use (or {@code null})
	 * @param password the password (or {@code null})
	 * @param httpClientOptions client options to use if the Apache HTTP Client is used
	 * @param timeout millisec
	 */
	public YuccaRestTemplate(String username, String password,int timeout,
			HttpClientOption... httpClientOptions) {
//		if (ClassUtils.isPresent("org.apache.http.client.config.RequestConfig", null)) {
			setRequestFactory(new CustomHttpComponentsClientHttpRequestFactory(timeout,
					httpClientOptions));
//		}
		addAuthentication(username, password);
		setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		});

	}

	private void addAuthentication(String username, String password) {
		if (username == null) {
			return;
		}
		List<ClientHttpRequestInterceptor> interceptors = Collections
				.<ClientHttpRequestInterceptor> singletonList(new BasicAuthorizationInterceptor(
						username, password));
		setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(),
				interceptors));
	}

	/**
	 * Options used to customize the Apache Http Client if it is used.
	 */
	public static enum HttpClientOption {

		/**
		 * Enable cookies.
		 */
		ENABLE_COOKIES,

		/**
		 * Enable redirects.
		 */
		ENABLE_REDIRECTS

	}

	private static class BasicAuthorizationInterceptor implements
			ClientHttpRequestInterceptor {

		private final String username;

		private final String password;

		public BasicAuthorizationInterceptor(String username, String password) {
			this.username = username;
			this.password = (password == null ? "" : password);
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body,
				ClientHttpRequestExecution execution) throws IOException {
			byte[] token = Base64.encode((this.username + ":" + this.password).getBytes());
			request.getHeaders().add("Authorization", "Basic " + new String(token));
			return execution.execute(request, body);
		}

	}

	protected static class CustomHttpComponentsClientHttpRequestFactory extends
			HttpComponentsClientHttpRequestFactory {

		private final String cookieSpec;

		private final boolean enableRedirects;
		
		private final int timeout;

		public CustomHttpComponentsClientHttpRequestFactory(int timeout,
				HttpClientOption[] httpClientOptions) {
			Set<HttpClientOption> options = new HashSet<YuccaRestTemplate.HttpClientOption>(
					Arrays.asList(httpClientOptions));
			this.cookieSpec = (options.contains(HttpClientOption.ENABLE_COOKIES) ? CookieSpecs.STANDARD
					: CookieSpecs.IGNORE_COOKIES);
			this.enableRedirects = options.contains(HttpClientOption.ENABLE_REDIRECTS);
			this.timeout = timeout;
		}

		@Override
		protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
			HttpClientContext context = HttpClientContext.create();
			context.setRequestConfig(getRequestConfig());
			return context;
		}

		protected RequestConfig getRequestConfig() {
			Builder builder = RequestConfig.custom().setCookieSpec(this.cookieSpec)
					.setAuthenticationEnabled(false)
					.setRedirectsEnabled(this.enableRedirects)
					.setConnectTimeout(timeout)
					.setSocketTimeout(timeout)
					;
			return builder.build();
		}

	}

}

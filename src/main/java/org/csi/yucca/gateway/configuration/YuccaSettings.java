/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.configuration;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "yucca")
public class YuccaSettings {

	@NotNull
	private Tenant tenant;
	@NotNull
	private Metadata metadata;
	@NotNull
	private Realtime realtime;
	@NotNull
	private A2A a2a;

	public static class Metadata {
		@NotNull
		private String httpEndpoint;
		@NotNull
		private String refreshCron;
		@NotNull
		private Boolean refreshOnStartup;
		
		@NotNull
		private Long timeToLive;
		
		public String getHttpEndpoint() {
			return httpEndpoint;
		}
		public void setHttpEndpoint(String httpEndpoint) {
			this.httpEndpoint = httpEndpoint;
		}
		public String getRefreshCron() {
			return refreshCron;
		}
		public void setRefreshCron(String refreshCron) {
			this.refreshCron = refreshCron;
		}
		public Boolean getRefreshOnStartup() {
			return refreshOnStartup;
		}
		public void setRefreshOnStartup(Boolean refreshOnStartup) {
			this.refreshOnStartup = refreshOnStartup;
		}
		public Long getTimeToLive() {
			return timeToLive;
		}
		public void setTimeToLive(Long timeToLive) {
			this.timeToLive = timeToLive;
		}
	}	

	public static class A2A {
		@NotNull
		private String httpEndpoint;
		@NotNull
		private Integer httpTimeout;
		@NotNull
		private Integer pollerMaxmessage;
		@NotNull
		private Integer pollerFixeddelay;
		public String getHttpEndpoint() {
			return httpEndpoint;
		}
		public void setHttpEndpoint(String httpEndpoint) {
			this.httpEndpoint = httpEndpoint;
		}
		public Integer getHttpTimeout() {
			return httpTimeout;
		}
		public void setHttpTimeout(Integer httpTimeout) {
			this.httpTimeout = httpTimeout;
		}
		public Integer getPollerMaxmessage() {
			return pollerMaxmessage;
		}
		public void setPollerMaxmessage(Integer pollerMaxmessage) {
			this.pollerMaxmessage = pollerMaxmessage;
		}
		public Integer getPollerFixeddelay() {
			return pollerFixeddelay;
		}
		public void setPollerFixeddelay(Integer pollerFixeddelay) {
			this.pollerFixeddelay = pollerFixeddelay;
		}

		
		
	}			
	public static class Realtime {
		@NotNull
		private String httpEndpoint;
		@NotNull
		private Integer httpTimeout;
		public String getHttpEndpoint() {
			return httpEndpoint;
		}
		public void setHttpEndpoint(String httpEndpoint) {
			this.httpEndpoint = httpEndpoint;
		}
		public Integer getHttpTimeout() {
			return httpTimeout;
		}
		public void setHttpTimeout(Integer httpTimeout) {
			this.httpTimeout = httpTimeout;
		}
		
	}
	
	public static class Tenant {
		@NotNull
		private String code;

		private String username;

		private String password;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}




	public Tenant getTenant() {
		return tenant;
	}




	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public Realtime getRealtime() {
		return realtime;
	}

	public void setRealtime(Realtime realtime) {
		this.realtime = realtime;
	}

	public A2A getA2a() {
		return a2a;
	}

	public void setA2a(A2A a2a) {
		this.a2a = a2a;
	}
	
	
	public String getConfigurationErrors() {
		String errors = "";
		if (getTenant()!=null && getTenant().getCode().isEmpty())
			errors += "yucca.tenant.code is missing \n";
		if (getTenant()!=null && getTenant().getUsername().isEmpty())
			errors += "yucca.tenant.username is missing \n";
		if (getTenant()!=null && getTenant().getPassword().isEmpty())
			errors += "yucca.tenant.password is missing \n";
		
		if (getRealtime()!=null && getRealtime().getHttpEndpoint().isEmpty())
			errors += "yucca.realtime.httpEndpoint is missing \n";
		
		if (getA2a()!=null && getA2a().getHttpEndpoint().isEmpty())
			errors += "yucca.a2a.httpEndpoint is missing \n";

		if (getA2a()!=null && getMetadata().getHttpEndpoint().isEmpty())
			errors += "yucca.metadata.httpEndpoint is missing \n";
		
		return errors.isEmpty()?null:errors;
	}

}

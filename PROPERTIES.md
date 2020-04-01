yucca-light properties 
=============

List of configurable properties.
Required properties must be declared in custom properties file. 

How to define properties
------------------------

If you run application in standalone mode you can simply write a file application.properties in the same directory of jar on in a subdirectory named config.  

If you deploy application in tomcat 8 you can choose one of this:

* Write the properties file in a custom dir and add in $CATALINA_BASE/conf/Context.xml the line:
```xml
<Parameter name="ext.application.properties" value="file:\your_custom_path\your_properties_file"/>
```
* Write the properties file in a custom dir and create in $CATALINA_BASE/conf/[enginename]/[hostname]/yucca-light.xml with:
```xml
<Context>
<Parameter name="ext.application.properties" value="file:\your_custom_path\your_properties_file"/>
</Context>
```

Properties
-----------

name | description | mandatory | default value
-----|-------------|---------- | ------------
security.user.name|username of user authorized to console and api of yucca-light|yes|admin
security.user.password|password of user authorized to console and api of yucca-light|yes|admin
yucca.realtime.httpEndpoint | HTTP Endpoint yucca for realtime | yes |   
yucca.a2a.httpEndpoint |  HTTP Endpoint yucca for A2A | yes  |
yucca.metadata.httpEndpoint | HTTP Endpoint yucca to retrieve stream metadata | yes |  
yucca.tenant.code | Registered tenant code (eg. sandbox) | yes|
yucca.tenant.username | Tenant tecnical username | yes|
yucca.tenant.password | Tenant tecnical password | yes| 
yucca.realtime.httpTimeout| HTTP Endpoint yucca for realtime timeout (ms) | no |10000
yucca.a2a.httpTimeout| HTTP Endpoint yucca for A2A timeout (ms) | no |10000
yucca.a2a.pollerMaxmessage| The maximum number of failed messages to receive for each poll (resending failed messages) | no |3
yucca.a2a.pollerFixeddelay| The fixed delay (ms) of poller (resending failed messages)| no |10000
yucca.metadata.refreshCron| Stream metadata refresh cron expression | no |5 * * * * *
yucca.metadata.refreshOnStartup| Stream metadata refresh at startup? | no | true
yucca.metadata.timeToLive|How long messages in final states are retained (in milliseconds)| no | 864000000 (10 days)
spring.activemq.broker-url| Url for external (or internal as default) ActiveMQ | no|vm:(broker:(mqtt://localhost:1883)?persistent=false)?marshal=false
spring.activemq.user| username for external (or internal as default) ActiveMQ | no|admin
spring.activemq.password| username for external (or internal as default) ActiveMQ | no|secret



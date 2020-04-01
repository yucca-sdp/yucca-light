create table if not exists EVENTS (GW_ID varchar(50), GW_INSERT_TIMESTAMP  long, 
								   GW_STATUS varchar(50), 
								   SOURCE_CODE varchar(50), 
								   STREAM_CODE varchar(50),
								   IS_APPLICATION boolean,
								   VALUES_JSON varchar(4096), 
								   LAST_ATTEMPT_ID varchar(50),
								   LAST_ATTEMPT_SEND_TIMESTAMP long,
								   LAST_ATTEMPT_RECEIVE_TIMESTAMP long,
								   LAST_RESPONSE varchar(4096),
								   LAST_ENDPOINT varchar(50),
								   NUM_ATTEMPT long
								   );
create table if not exists ATTEMPT_HISTORY (
								   GW_ID varchar(50), 
								   ATTEMPT_ID varchar(50),
								   ATTEMPT_SEND_TIMESTAMP long,
								   ATTEMPT_RECEIVE_TIMESTAMP long,
								   FROM_STATUS varchar(50), 
								   TO_STATUS varchar(50), 
								   RESPONSE varchar(50),
								   RESPONSE_DETAIL varchar(4096),
								   ENDPOINT varchar(50));
								   
create table if not exists STREAM_METADATA_CONFIGURATION (
								   TENANT_CODE varchar(50), 
								   STREAM_CODE varchar(100), 
								   VIRTUALENTITY_CODE varchar(100),
								   DEPLOYMENT_VERSION long,
								   STREAM_NAME varchar(50),
								   VIRTUALENTITY_NAME varchar(50),
								   VIRTUALENTITY_DESCRIPTION varchar(250),
								   VIRTUALENTITY_TYPE varchar(50),
								   VIRTUALENTITY_CATEGORY varchar(50),
								   LASTUPDATE_TIMESTAMP long,
								   METADATA_JSON varchar(500000),
								   SCHEMA_JSON varchar(10000),
								   CONSTRAINT pk_stream_metadata_configuration 
								   	PRIMARY KEY (TENANT_CODE,STREAM_CODE,VIRTUALENTITY_CODE,DEPLOYMENT_VERSION));								   
commit;
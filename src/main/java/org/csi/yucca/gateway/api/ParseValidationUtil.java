/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 Regione Piemonte
 * 
 */
package org.csi.yucca.gateway.api;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.csi.yucca.gateway.api.dto.Measure;
import org.csi.yucca.gateway.api.dto.StreamSensorEvent;
import org.csi.yucca.gateway.configuration.StreamConfigurationDAO;
import org.csi.yucca.gateway.configuration.dto.StreamMetadata;
import org.csi.yucca.gateway.exception.InsertApiBaseException;
import org.csi.yucca.gateway.util.Conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class ParseValidationUtil {
	private static final Logger log=Logger.getLogger("org.csi.yucca.datainsert");
	public static final String GENERIC_SCHEMA = "stream_sensor_schema.json";

    public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String _msDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String _secDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ssZ";
	
    
    public static boolean isValidVersusSchema(JsonNode json, String schemaStr) throws IOException,ProcessingException
	{
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		final JsonSchema schema;
		ProcessingReport pr;
				
		JsonNode schemaNode = JsonLoader.fromResource( "/"+schemaStr);
		schema = factory.getJsonSchema(schemaNode);
        pr = schema.validate(json);
		return pr.isSuccess();
	}

	
	public static boolean isValidVersusSchema(JsonNode json, String tenantCode, String streamCode, String virtualentityCode, StreamConfigurationDAO streamConfigurationDAO) throws InsertApiBaseException, IOException, ProcessingException
	{
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		final JsonSchema schema;
		ProcessingReport pr;
				
		StreamMetadata streamMetadataConfiguration = streamConfigurationDAO.findLastStreamMetadataConfiguration(tenantCode, streamCode, virtualentityCode);
		if(streamMetadataConfiguration==null || streamMetadataConfiguration.getSchemaJson()==null){
			log.error("ERROR: Json schema not found - tenant code: " + tenantCode +" | stream code: " + streamCode  +" | virtualentity code: " + virtualentityCode);
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, json.toString());
		}
		
		JsonNode schemaNode = JsonLoader.fromString(streamMetadataConfiguration.getSchemaJson());
		

		schema = factory.getJsonSchema(schemaNode);
        pr = schema.validate(json);
		return pr.isSuccess();
	}


	public static boolean validateEventTime(StreamSensorEvent eventDto) {
		if (eventDto.getValues() != null) {
			for (Measure measure : eventDto.getValues()) {
				Date o = Conversion.fromStringToDate(measure.getTime());
				if (o==null)
					return false;
			}
			return true;			
		}
		return false;
	}	
	
	
//	public HashMap<String, DatasetBulkInsert> parseJsonInputStream(String tenant, String jsonInput) throws Exception {
//		int i =0;
//		boolean endArray=false;
//		JSONObject ooo=null;
//		HashMap<String, DatasetBulkInsert> ret= new HashMap<String, DatasetBulkInsert>();
//		
//		int totalDocumentsToIns=0;
//		String sensor=null;
//		String application=null;
//		String stream=null;
//		String streamToFind=null;
//		while (i<100000 && !endArray) {
//			try {
//				ooo = JsonPath.read(jsonInput, "$["+i+"]");
//				if (null==ooo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DATA_NOTARRAY);
//				 sensor=(String)ooo.get("sensor");
//				application=(String)ooo.get("application");
//				stream=(String)ooo.get("stream");
//
//
//
//				streamToFind=(stream!=null ? stream: application);
//
//				//TODO non so se e' bloccante ... 
//				if (streamToFind == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
//				if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);
//
//
//
//				if (ret.get(sensor+"_"+streamToFind)!=null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_DUPLICATE, " for stream "+streamToFind);
//
//
//				DatasetBulkInsert datiToins=parseMisura(tenant, ooo);
//
//				//TODO .. controllo sul numero di record da inserire
//
//				datiToins.setStream(streamToFind);
//				datiToins.setSensor(sensor);
//				datiToins.setStatus(DatasetBulkInsert.STATUS_SYNTAX_CHECKED);
//				
//				totalDocumentsToIns=totalDocumentsToIns+datiToins.getNumRowToInsFromJson();
//
//				ret.put(sensor+"_"+streamToFind, datiToins);
//
//
//				i++;
//			} catch (PathNotFoundException e) {
//				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) {
//					endArray=true;
//				} else {
//					log.error("[InsertApiLogic::parseJsonInputStream] PathNotFoundException imprevisto --> " + e );
//					throw e;
//				}
//			} catch (Exception ex) {
//				log.error("[InsertApiLogic::parseJsonInputStream] GenericEsxception" + ex );
//				i++;
//				endArray=true;
//				throw ex;
//			} finally {
//				//System.out.println(" TIMETIME parseJsonInputDataset -- fine metodo--> "+System.currentTimeMillis());
//				
//				
//			}
//		}
//		return ret;
//
//	}	
//
//	private DatasetBulkInsert parseMisura(String tenant, JSONObject bloccoDaIns) throws Exception {
//		//Integer datasetVersion=JsonPath.read(jsonInput, "$.datasetVersion");
//		Integer datasetVersion=(Integer)bloccoDaIns.get("datasetVersion");
//		int reqVersion=-1;
//		DatasetBulkInsert ret=null;
//
////		String stream=JsonPath.read(jsonInput, "$.stream");
////		String sensor=JsonPath.read(jsonInput, "$.sensor");
////		String application=JsonPath.read(jsonInput, "$.application");
//
//		String stream=(String)bloccoDaIns.get("stream");
//		String sensor=(String)bloccoDaIns.get("sensor");
//		String application=(String)bloccoDaIns.get("application");
//		
//		
//		if (application == null && stream == null) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_STREAM_MANCANTE);
//		if (sensor == null ) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_SENSOR_MANCANTE);
//		if (null!=datasetVersion) reqVersion=datasetVersion.intValue();
//
//		SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
//		ArrayList<MongoStreamInfo> elencoStream=mongoAccess.getStreamInfo(tenant, (stream!=null ? stream : application), sensor);
//
//		if (elencoStream==null || elencoStream.size()<=0) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_STREAM_NOT_FOUND, ": "+(stream!=null ? stream : application) +" (sensor: "+sensor+")");
//		ArrayList<FieldsMongoDto> elencoCampi=mongoAccess.getCampiDataSet(elencoStream, Long.parseLong(""+reqVersion) );
//
//		if (elencoCampi==null || elencoCampi.size()<=0) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID, ": "+(stream!=null ? stream : application) +" (sensor: "+sensor+")");
//		HashMap<String, FieldsMongoDto> campiMongo= new HashMap<String, FieldsMongoDto>();
//		long idDatasetTrovato=-1;
//		long datasetVersionTrovato=-1;
//		for (int i = 0; i< elencoCampi.size();i++) {
//			campiMongo.put(elencoCampi.get(i).getFieldName(), elencoCampi.get(i));
//			idDatasetTrovato=elencoCampi.get(i).getDatasetId();
//			datasetVersionTrovato=elencoCampi.get(i).getDatasetVersion();
//		}
//		//JSONObject ooo=null;
//		JSONObject components=null;
//		int i =0;
//		boolean endArray=false;
//
//		String insStrConstBase= "streamCode : \"" + (stream!=null ? stream : application) +"\"";
//		insStrConstBase+= " , idDataset : "+idDatasetTrovato;
//		insStrConstBase+= " , datasetVersion : "+datasetVersionTrovato;
//		insStrConstBase+= " , sensor : \""+sensor+"\"";
//
//		//			    "sensor" : "88c8dfb2-6323-5445-bf7d-6af67f0166b6",
//		//			    "time" : ISODate("2014-09-03T05:35:00.000Z"),
//		//			    "idDataset" : 4,
//		//			    "datasetVersion" : 1,
//		//			    "streamCode" : "TrFl",
//
//		JSONArray arrayValori=(JSONArray)bloccoDaIns.get("values");
//		ArrayList<String> rigadains= new ArrayList<String>();
//		int numeroCampiMongo=elencoCampi.size();
//		FieldsMongoDto campotimeStamp=null;
//		String timeStamp=null;
//		campotimeStamp= new FieldsMongoDto("aaa",FieldsMongoDto.DATA_TYPE_DATETIME);
//		
//		JSONObject curElem=null;
//		while (i<arrayValori.size() && !endArray) {
//			try {
//				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") inizio--> "+System.currentTimeMillis());
//				
//				
//				curElem=(JSONObject)arrayValori.get(i);
//				
//				
//				components=(JSONObject)curElem.get("components");
//				
//
//				//Controllo del timeStamp
//				timeStamp= (String)curElem.get("time");
//
//				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") recuperati oggetti--> "+System.currentTimeMillis());
//				
//				if (!campotimeStamp.validateValue(timeStamp))  throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
//						" - field time ("+insStrConstBase+"): "+timeStamp);
//
//
//
//
//				//insStrConst+= ", time: {$date :\""+timeStamp+"\"} ";
//				//rigadains.add(parseComponents(components, insStrConst, elencoCampi));
//				
//				rigadains.add(parseComponents(components, insStrConstBase+", time: {$date :\""+timeStamp+"\"} ", campiMongo));
//				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") parsing components--> "+System.currentTimeMillis());
//				
//
//
//				//ora controllo che tra i campi che arrivano dalla cfg non ce ne sia qualcuno che non era presente nel json in input
//				
//				if (components.keySet().size()!=numeroCampiMongo) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE);
//				
//				
////				Iterator<String> itCampiCheck=campiMongo.keySet().iterator();
////				while (itCampiCheck.hasNext()) {
////					String nome=itCampiCheck.next();
////					FieldsMongoDto campoMongo=campiMongo.get(nome);
////					if (campoMongo.isSuccessChecked()==false) throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_INPUT_INVALID_DATA_VALUE,
////							" - field "+nome+" ("+insStrConst+") not found in input data : "+((JSONObject)arrayValori.get(i)).toJSONString()); 
////					(campiMongo.get(nome)).setSuccessChecked(false);
////				}
//
//
//				i++;
//				//System.out.println(" TIMETIME parseMisura -- valore ("+i+") fine--> "+System.currentTimeMillis());
//
//
//			} catch (PathNotFoundException e) {
//				if (e.getCause() instanceof java.lang.IndexOutOfBoundsException) endArray=true;
//			}
//
//		}
//		ret= new DatasetBulkInsert();
//		ret.setDatasetVersion(datasetVersionTrovato);
//		ret.setIdDataset(idDatasetTrovato);
//		ret.setNumRowToInsFromJson(i);
//		ret.setRowsToInsert(rigadains);
//		return ret;
//
//	}		
}

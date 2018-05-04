package org.renci.comet;

import org.apache.accumulo.core.client.AccumuloException;

import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.apache.hadoop.io.Text;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.renci.comet.accumuloops.AccumuloOperationsApiIfce;
import org.renci.comet.accumuloops.AccumuloOperationsApiImpl;

public class CometOps implements CometOpsIfce {
	String instanceName = "docker-development";
    String zooServers = "172.16.100.5,172.16.100.4,172.16.100.1"; // Provide list of zookeeper server here. For example, localhost:2181
    String userName = "root"; // Provide username
    String password = "secret"; // Provide password
    public static String ERROR = "error";
	public static String SUCCESS = "Success";
    private static final Logger log = Logger.getLogger(AccumuloOperationsApiImpl.class);
    public String tableName="accu-test";
    
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
    
    public JSONObject writeScope (String contextID, String family, String key, String scopeValue, String readToken, String writeToken) throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException {
	    Instance inst = new ZooKeeperInstance(instanceName,zooServers);

		Connector conn = inst.getConnector(userName, password);
		
		AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl();
		
		Text rowID = new Text(contextID);
		Text colFam = new Text(family);
		Text colQual = new Text(key);
		Text vis = new Text(readToken);
		String[] value = {"false", writeToken, scopeValue};
		
		byte[] serializedByteArray = null;
		try {
			serializedByteArray = serialize(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		org.apache.accumulo.core.data.Value accumuloValue = new org.apache.accumulo.core.data.Value(serializedByteArray);
		
		JSONObject output = accu.addAccumuloRow(conn, tableName, rowID, colFam, colQual, accumuloValue, vis);
	    
	    return output;
	}
    
    public JSONObject deleteScope(String contextID, String family, String key, String readToken, String writeToken) throws AccumuloException, AccumuloSecurityException {
    		Instance inst = new ZooKeeperInstance(instanceName,zooServers);

		Connector conn = inst.getConnector(userName, password);
		AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl();
		JSONObject output = new JSONObject();
		Authorizations auth = null;

		Text rowID = new Text(contextID);
		Text colFam = new Text(family);
		Text colQual = new Text(key);
		
		if (readToken != null) {
			auth = new Authorizations(readToken);
		} else {
			auth = new Authorizations();
		}
		
		try {
			Scanner scan = conn.createScanner(tableName, auth);
			scan.setRange(new Range(contextID, contextID));
			Text vis = new Text(readToken);
			//public JSONObject deleteAccumuloRow(Connector conn, Scanner scanner, String tableName, Text colFam, Text colQual, Text visibility)
			output = accu.deleteAccumuloRow(conn,  scan, tableName, colFam, colQual, vis);

		} catch (TableNotFoundException e) {
			log.error("DeleteEntry failed due to: " + e.getMessage());
			try {
				return output.put(ERROR, "Failed to delete entry due to " + e.getMessage());
			} catch (JSONException e1) {
				log.error("JSON Exception: " + e1.getMessage());
			}

		} catch (Exception e) {
			log.error("DeleteEntry failed due to: " + e.getMessage());
			try {
				return output.put(ERROR, "Failed to delete entry due to " + e.getMessage());
			} catch (JSONException e1) {
				log.error("JSON Exception: " + e1.getMessage());
			}

		}

		return output;
	}
    
    public JSONObject readScope (String contextID, String family, String key, String readToken) throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException {
	    
    		Instance inst = new ZooKeeperInstance(instanceName,zooServers);

		Connector conn = inst.getConnector(userName, password);
		
		AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl();
		
		Text rowID = new Text(contextID);
		Text colFam = new Text(family);
		Text colQual = new Text(key);
		Authorizations auths = new Authorizations(readToken);
		String scopeValue = null;
		JSONObject output = new JSONObject();
		
		/*try {
			Scanner scan = conn.createScanner(contextID, auths);
			scan.setRange(new Range(contextID, contextID));
			scan.fetchColumn(colFam, colQual);

			for (Entry<Key, Value> entry : scan) {
				scopeValue = entry.getValue().toString();
			}

		} catch (TableNotFoundException e) {
			log.error("Unable to find table " + tableName);

			try {
				return output.put(ERROR, "Table not found.");
			} catch (JSONException e1) {
				log.error("JSON Exception: " + e1.getMessage());
			}
		} catch (Exception e) {
			try {
				return output.put(ERROR,"Failed to read scope.");
			} catch (JSONException e1) {
				log.error("JSON Exception: " + e1.getMessage());

			}
		}*/
		
		//public Scanner readOneRow(Connector conn, String tableName, String rowID, String visibility)
	    output = accu.readOneRow(conn, tableName, rowID, readToken);
	    
	    return output;
	}
    
    public JSONObject enumerateScopes(String contextID, String family, String readToken) throws AccumuloException, AccumuloSecurityException {
    		Instance inst = new ZooKeeperInstance(instanceName,zooServers);
    		Connector conn = inst.getConnector(userName, password);
		//Scan method in accumulo with proper auuthorization labels
		JSONObject output = new JSONObject();
		Authorizations auth = null;

		if (readToken != null) {
			auth = new Authorizations(readToken);

		} else {
			auth = new Authorizations();
		}

		try {
			Scanner scanner = conn.createScanner(tableName, auth);
			scanner.setRange(new Range(contextID,contextID)); //constructor empty implies the whole table (startKey=-neg, endKey=-pos)

			for (Entry<Key, Value> entry : scanner) {
				if(entry.getKey().compareColumnFamily(new Text(contextID))==0) {
					Value v = entry.getValue();
					Text valueText = new Text(v.get());
					Key k = entry.getKey();
					Text scopeName = k.getColumnQualifier();
					output.put(scopeName.toString(), valueText.toString());
				}
			}
		} catch (TableNotFoundException e) {
			log.error("DeleteEntry failed due to: " + e.getMessage());
			try {
				return output.put(ERROR, "Failed to delete entry due to " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			log.error("DeleteEntry failed due to: " + e.getMessage());
			try {
				return output.put(ERROR, "Failed to delete entry due to " + e.getMessage());
			} catch (JSONException e1) {
				log.error("Json Exception: "  + e1.getMessage());
			}
		}
		return output;
    }
}

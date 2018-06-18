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
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.renci.comet.accumuloops.AccumuloOperationsApiIfce;
import org.renci.comet.accumuloops.AccumuloOperationsApiImpl;

public class CometOps implements CometOpsIfce {
	String instanceName = "aws-development";
    String zooServers = "zoo1,zoo2,zoo3"; // Provide list of zookeeper server here. For example, localhost:2181
    String userName = "root"; // Provide username
    String password = "secret"; // Provide password
    public String tableName="trace"; //provide Accumulo table name
    
    public static String ERROR = "error";
	public static String SUCCESS = "Success";
	public static final int NUM_OF_SERIALIZED_PARAMETERS = 5;
    private static final Logger log = Logger.getLogger(AccumuloOperationsApiImpl.class);
    public static final boolean CHECK_TOKEN_STRENGTH = false;
    public static final boolean CHECK_CLIENT_CERT = true;

	/**
	 * Check if password contains:
	 * At least 8 chars
	 * Contains at least one digit
	 * Contains at least one lower alpha char and one upper alpha char
	 * Contains at least one char within a set of special chars (@#%$^ etc.)
	 * Does not contain space, tab, etc.
	 *
	 * ^                 # start-of-string
	 * (?=.*[0-9])       # a digit must occur at least once
	 * (?=.*[a-z])       # a lower case letter must occur at least once
	 * (?=.*[A-Z])       # an upper case letter must occur at least once
	 * (?=.*[@#$%^&+=])  # a special character must occur at least once
	 * (?=\S+$)          # no whitespace allowed in the entire string
	 * .{8,}             # anything, at least eight places though
	 * $                 # end-of-string
	 *
	 * @param expression
	 * @return
	 */
	public static boolean isTokenStrong(String expression) {
		if (!CometOps.CHECK_TOKEN_STRENGTH)
			return true;
	    if (expression != null) {
	        return expression.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
	    }
	    return false;
	}

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
		
		Map<String, Value> mapOutput = new HashMap<String, Value>();
		//System.out.println("Starting accu.readOneRow(conn, readToken, rowID, colFam, colQual, vis.toString())");
		mapOutput = accu.readOneRow(conn, tableName, rowID, colFam, colQual, readToken);
		//System.out.println("Ended accu.readOneRow(conn, readToken, rowID, colFam, colQual, vis.toString())");
		int mapSize = mapOutput.size();
		if (mapSize == 0) {
			log.debug("No scope, creating new one");
		} else if (mapSize != 1) {
			log.debug("Conflict in Accumulo record, please clean up");
		} else {
			for (Map.Entry<String, Value> entry : mapOutput.entrySet()) {
		    		Value v = entry.getValue();
		    		log.debug("Comet readscope: got Value: " + v);
		    		String[] deserialized = null;
				try {
					deserialized = (String[]) deserialize(v.get());
				} catch (ClassNotFoundException | IOException e1) {
					log.error("deserialization failed: " + e1);
				}
		    		if (deserialized != null && deserialized.length == CometOps.NUM_OF_SERIALIZED_PARAMETERS) {
		    			log.debug("deserialized values:");
		    			for (String s : deserialized)
		    				log.debug(s);
		    			log.debug("deserialized values done.");
		    			if (deserialized[0].equals("true")) {
			    			try {
			    				JSONObject output = new JSONObject();
			    				output.put(ERROR, "Failed to write scope: scope already deleted.");
			    				return output;
						} catch (JSONException e) {
							e.printStackTrace();
						}
		    			}
		    		}
		    }
		}
		
		//Accumulo value field format: {ifDeleted, writeToken, scopeValue, Comet_version, deletionTimeStamp}
		String[] value = {"false", writeToken, scopeValue, CometInitializer.COMET_VERSION, ""};
		log.debug("Writing in scope: contextID: " + contextID + "\n family: " + family + "\n key: " + key + "\n readToken: " + readToken);
		//for (String s : value)
		//	System.out.println(s);

		byte[] serializedByteArray = null;
		try {
			serializedByteArray = serialize(value);
			log.debug("Serialize successful!");
		} catch (IOException e) {
			log.debug(e.toString());
		}

		org.apache.accumulo.core.data.Value accumuloValue = new org.apache.accumulo.core.data.Value(serializedByteArray);

		log.debug("Writing to Accumulo done!");

		JSONObject jsonOutput = accu.addAccumuloRow(conn, tableName, rowID, colFam, colQual, accumuloValue, vis);

	    return jsonOutput;
	}

	public JSONObject deleteScope (String contextID, String family, String key, String readToken, String writeToken) throws AccumuloException, AccumuloSecurityException, TableNotFoundException, JSONException {

		//public JSONObject readOneRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, String visibility)

		Instance inst = new ZooKeeperInstance(instanceName,zooServers);
		//System.out.println("ReadScope: instance initiated");

		Connector conn = inst.getConnector(userName, password);
		//System.out.println("read scope: got connection");
		AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl();
		JSONObject jsonOutput = new JSONObject();

		Text rowID = new Text(contextID);
		Text colFam = new Text(family);
		Text colQual = new Text(key);
		Text vis = new Text(readToken);
		Map<String, Value> mapOutput = new HashMap<String, Value>();
		//System.out.println("Starting accu.readOneRow(conn, readToken, rowID, colFam, colQual, vis.toString())");
		mapOutput = accu.readOneRow(conn, tableName, rowID, colFam, colQual, readToken);
		//System.out.println("Ended accu.readOneRow(conn, readToken, rowID, colFam, colQual, vis.toString())");


		int mapSize = mapOutput.size();
		if (mapSize == 0) {
			jsonOutput.put(ERROR, "Failed to delete scope: no such entry");
			return jsonOutput;
		}

		if (mapSize != 1) {
			jsonOutput.put(ERROR, "Failed to delete scope: conflict in Accumulo record, please clean up");
			return jsonOutput;
		}

		for (Map.Entry<String, Value> entry : mapOutput.entrySet()) {
	    		Value v = entry.getValue();
	    		String[] deserialized = null;
			//System.out.println("Enumerate scope key values: ");
	    		//System.out.printf("Key: %-60s Value: %s\n", entry.getKey(), entry.getValue());
			try {
				deserialized = (String[]) deserialize(v.get());
			} catch (ClassNotFoundException | IOException e1) {
				log.error("deserialization failed: " + e1);
				jsonOutput.put(ERROR, "Failed to delete scope: deserialization failed");
			}
	    		if (deserialized != null && deserialized.length == CometOps.NUM_OF_SERIALIZED_PARAMETERS) {
	    			if (!writeToken.equals(deserialized[1])) {
	    				try {
						jsonOutput.put(ERROR, "Cannot delete scope: incorrect write token");
					} catch (JSONException e) {
						log.error("JSON Exception: " + e.getMessage());
					}
	    				log.error("Cannot delete scope: incorrect write token");
	    				return jsonOutput;
	    			}

	    			deserialized[0] = "true";
	    			
	    			long unixTimestamp = Instant.now().getEpochSecond();
	    			String strLong = Long.toString(unixTimestamp);
	    	        //System.out.println(strLong);
	    			deserialized[4] = strLong;
	    	        
	    			byte[] serializedByteArray = null;
	    			try {
	    				serializedByteArray = serialize(deserialized);
	    				org.apache.accumulo.core.data.Value accumuloValue = new org.apache.accumulo.core.data.Value(serializedByteArray);

		    			JSONObject output = accu.addAccumuloRow(conn, tableName, rowID, colFam, colQual, accumuloValue, vis);

		    			try {
						jsonOutput.put(SUCCESS, "Scope deleted");
						} catch (JSONException e) {
							log.error("JSON Exception: " + e.getMessage());
						}

	    			} catch (IOException e) {
	    				log.error("IO Exception: " + e.getMessage());
	    			}
	    		}
		}

		return jsonOutput;
	}


    public JSONObject readScope (String contextID, String family, String key, String readToken) throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException {

    		Instance inst = new ZooKeeperInstance(instanceName,zooServers);
    		//System.out.println("read scope: instance initiated");

		Connector conn = inst.getConnector(userName, password);
		//System.out.println("read scope: got connection");

		AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl();

		Text rowID = new Text(contextID);
		Text colFam = new Text(family);
		Text colQual = new Text(key);
		String scopeValue = null;
		Map<String, Value> output = new HashMap<String, Value>();

		JSONObject jsonOutput = new JSONObject();
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

		//public JSONObject readOneRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, String visibility)
	    output = accu.readOneRow(conn, tableName, rowID, colFam, colQual, readToken);
	    for (Map.Entry<String, Value> entry : output.entrySet()) {
	    		Value v = entry.getValue();
	    		//System.out.println("Comet readscope: got Value: " + v);
	    		String[] deserialized = null;
			try {
				deserialized = (String[]) deserialize(v.get());
			} catch (ClassNotFoundException | IOException e1) {
				log.error("deserialization failed: " + e1);
			}
	    		if (deserialized != null && deserialized.length == CometOps.NUM_OF_SERIALIZED_PARAMETERS) {
	    			log.debug("deserialized values:");
	    			for (String s : deserialized)
	    				log.debug(s);
	    			log.debug("deserialized values done.");
	    			if (deserialized[0].equals("false")) {
		    			try {
						jsonOutput.put(entry.getKey(), deserialized[2]);
					} catch (JSONException e) {
						e.printStackTrace();
					}
	    			}
	    		}
	    }
	    return jsonOutput;
	}

	public JSONObject enumerateScopes(String contextID, String readToken) throws AccumuloException, AccumuloSecurityException {

	    	Instance inst = new ZooKeeperInstance(instanceName,zooServers);
			//System.out.println("read scope: instance initiated");

		Connector conn = inst.getConnector(userName, password);
		//System.out.println("read scope: got connection");

		AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl();

		Text rowID = new Text(contextID);
		String scopeValue = null;
		Map<String, Value> output = new HashMap<String, Value>();

		JSONObject jsonOutput = new JSONObject();

		try {
			//System.out.println("Starting accu.enumerateRows(conn, contextID, contextID, readToken)");
			output = accu.enumerateRows(conn, tableName, rowID, readToken);
			//System.out.println("Ended accu.enumerateRows(conn, contextID, contextID, readToken)");
		} catch (TableNotFoundException e2) {
			// TODO Auto-generated catch block
			log.error("Table not found: " + e2);
		}


		for (Map.Entry<String, Value> entry : output.entrySet()) {
			//System.out.println("Enumerate scope key values: ");
    			//System.out.printf("Key: %-60s Value: %s\n", entry.getKey(), entry.getValue());
	    		Value v = entry.getValue();
	    		String[] deserialized = null;
				try {
					deserialized = (String[]) deserialize(v.get());
				} catch (ClassNotFoundException | IOException e1) {
					log.error("deserialization failed: " + e1);
				}
	    		if (deserialized != null && deserialized.length == CometOps.NUM_OF_SERIALIZED_PARAMETERS) {
	    			log.debug("deserialized values:");
	    			for (String s : deserialized)
	    				log.debug(s);
	    			log.debug("deserialized values done.");
	    			if (deserialized[0].equals("false")) {
		    			try {
						jsonOutput.put(entry.getKey(), deserialized[2]);
					} catch (JSONException e) {
						e.printStackTrace();
					}
	    			}
	    		}
	    }
	    return jsonOutput;
    }
}

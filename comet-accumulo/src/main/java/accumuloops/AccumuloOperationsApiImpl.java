package accumuloops;

import org.apache.accumulo.core.cli.ScannerOpts;
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
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.apache.hadoop.io.Text;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class AccumuloOperationsApiImpl implements AccumuloOperationsApiIfce {
	public static String ERROR = "error";
	public static String SUCCESS = "Success";
	private static final Logger log = Logger.getLogger(AccumuloOperationsApiImpl.class);
	
/*	*//**
	 * Create new Zookeeper instance. 
	 * @param instanceName
	 * @param zooServers
	 * @return Instance
	 *//*
    public Instance createZooKeeperInstance(String instanceName, String zooServers) {
    		Instance inst = new ZooKeeperInstance(instanceName,zooServers);
    		return inst;
    }
    
    *//**
	 * Create new Accumulo connector. 
	 * @param userName
	 * @param password
	 * @return AccuConnector
	 *//*
    public Connector getAccumuloConnector(Instance inst, String userName, String password) {
    		Connector conn = inst.getConnector(userName, password);
    		return conn;
    }*/
	
	/**
	 * Create unified JSONObject
	 * @param message
	 * @return
	 */
    public JSONObject createJSONObject(String messageType, String message) {
		JSONObject output = new JSONObject();
		try {
			output.put(ERROR, message);
		} catch (JSONException e1) {
			log.error("JSON Exception: " + e1.getMessage());
		}
		return output;
    }
    
    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @return 
     * @throws TableExistsException 
     * @throws AccumuloSecurityException 
     * @throws AccumuloException 
	 */    
    public JSONObject createAccumuloTable(Connector conn, String tableName) throws AccumuloException, AccumuloSecurityException, TableExistsException {
    		TableOperations ops = conn.tableOperations();
    		if (ops.exists(tableName)) {
			return createJSONObject(ERROR, "Unable to create table: " + tableName + ". Table exists");
		}
    		ops.create(tableName);
    		return createJSONObject(SUCCESS, "success");
    }
    
    /**
	 * Delete Accumulo table.
	 * @param tableName
	 * @return 
     * @throws TableNotFoundException 
     * @throws AccumuloSecurityException 
     * @throws AccumuloException 
	 */       
    public JSONObject deleteAccumuloTable(Connector conn, String tableName) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
    		JSONObject output = new JSONObject();
    		TableOperations ops = conn.tableOperations();
    		if (ops.exists(tableName)) {
    			ops.delete(tableName);
    			createJSONObject(SUCCESS, "success, deleted table: " + tableName);
    		} else {
    			createJSONObject(ERROR, "Unable to delete table: " + tableName + ". Table notfound");
    		}
    		return output;
    }
    
    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param value
	 * @param visibility
	 * @return 
     * @throws TableNotFoundException 
     * @throws MutationsRejectedException 
	 */    
    public JSONObject addAccumuloRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Value value, Text visibility) throws TableNotFoundException, MutationsRejectedException {
    		JSONObject output = new JSONObject();
    		@SuppressWarnings("deprecation")
		BatchWriter bw = conn.createBatchWriter(tableName,1000000, 60000, 2);
    		Mutation mutation = new Mutation(rowID);
    		mutation.put(colFam, colQual, value);
    		bw.addMutation(mutation);
    		try {
			output.put(SUCCESS, "success, deleted table: " + tableName);
		} catch (JSONException e1) {
			log.error("JSON Exception: " + e1.getMessage());
		}
    		return output;
    }

    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return 
     * @throws TableNotFoundException 
	 */        
    public JSONObject deleteAccumuloRow(Connector conn, Scanner scanner, String tableName, Text family, Text colFam, Text colQual, Text visibility) throws TableNotFoundException {
    		Mutation deleter = null;
		for (Entry<Key, Value> entry : scanner) {
			if (deleter == null) {
				deleter = new Mutation(entry.getKey().getRow());

			}
			if (visibility != null) {
				deleter.putDelete(new Text(family), new Text(colFam), new ColumnVisibility(visibility));
			} else {
				deleter.putDelete(entry.getKey().getColumnFamily(), entry.getKey().getColumnQualifier());
			}
		}
		
		try {
			if (deleter != null) {
				BatchWriter bw = conn.createBatchWriter(tableName,1000000, 60000, 2);
				bw.addMutation(deleter);
				bw.close();
			}
		} catch (MutationsRejectedException e) {
			//log.error("Error deleting record. Reason: " + e.getMessage());
			return createJSONObject(ERROR, "Error deleting record. Reason: " + e.getMessage());
		}

		if (deleter != null) {
			return createJSONObject(ERROR, "ContextID: " + deleter.getRow().toString() + '\n');
		}
		return createJSONObject(ERROR, "Error: No scope to delete. \n");
    }
    

    
    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param visibility
	 * @param numberOfThreads
	 * @return 
     * @throws TableNotFoundException 
	 */       
    public JSONObject enumerateTable(Connector conn, String tableName, String visibility, String numberOfThreads) throws TableNotFoundException {
    		JSONObject output = new JSONObject();
    		
    		Authorizations auth = null;
    		if (visibility != null) {
    			auth = new Authorizations(visibility);
    		} else {
    			auth = new Authorizations();
    		}
    		
    		try {
    			Scanner scan = conn.createScanner(tableName, auth);
    			scan.setRange(new Range());

    			Iterator<Map.Entry<Key,Value>> iterator = scan.iterator();
    			  
    			while (iterator.hasNext()) {
    			   Map.Entry<Key,Value> entry = iterator.next();
    			   Key key2 = entry.getKey();
    			   Value value = entry.getValue();
    			   output.put(key2.toString(), value);
    			//   output.append(key2.toString(), value);
    			//  System.out.println(key2 + " ==> " + value);
    			}
    		} catch (TableNotFoundException e) {
    			log.error("enumerateTable failed due to: " + e.getMessage());
    			try {
    				return output.put(ERROR, "Failed to delete entry due to " + e.getMessage());
    			} catch (JSONException e1) {
    				log.error("JSON Exception: " + e1.getMessage());
    			}
    		} catch (Exception e) {
    			log.error("enumerateTable failed due to: " + e.getMessage());
    			try {
    				return output.put(ERROR, "Failed to delete entry due to " + e.getMessage());
    			} catch (JSONException e1) {
    				log.error("JSON Exception: " + e1.getMessage());
    			}
    		}
    		return output;
    }

    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param conn
	 * @param tableName
	 * @param rowID
	 * @return 
     * @throws TableNotFoundException 
	 */  
    public Scanner readRow(Connector conn, String tableName, Text rowID) throws TableNotFoundException {
    		ScannerOpts scanOpts = new ScannerOpts();
    		// Create a scanner
        Scanner scanner = conn.createScanner(tableName, Authorizations.EMPTY);
        scanner.setBatchSize(scanOpts.scanBatchSize);
        // Say start key is the one with key of row
        // and end key is the one that immediately follows the row
        scanner.setRange(new Range(rowID));
        return scanner;
    }

}

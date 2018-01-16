package accumuloops;

import org.apache.accumulo.core.client.AccumuloException;

import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
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
	 * Create new Accumulo table.
	 * @param tableName
	 * @return 
     * @throws TableExistsException 
     * @throws AccumuloSecurityException 
     * @throws AccumuloException 
	 */    
    public JSONObject createAccumuloTable(Connector conn, String tableName) throws AccumuloException, AccumuloSecurityException, TableExistsException {
	    JSONObject output = new JSONObject();
    		TableOperations ops = conn.tableOperations();
    		if (ops.exists(tableName)) {
    			try {
    				return output.put(ERROR, "Unable to create table. Table exists");
    			} catch (JSONException e1) {
    				log.error("JSON Exception: " + e1.getMessage());
    			}
		}
    		ops.create(tableName);
    		try {
			output.put(SUCCESS, "success");
		} catch (JSONException e1) {
			log.error("JSON Exception: " + e1.getMessage());
		}
    		return output;
    }
    
    /**
	 * Delete Accumulo table.
	 * @param tableName
	 * @return 
	 */       
    public JSONObject deleteAccumuloTable(String tableName) {
		return null;
    		
    }
    
    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param value
	 * @param visibility
	 * @return 
	 */    
    public JSONObject addAccumuloRow(String tableName, Text colFam, Text colQual, Text value, Text visibility) {
		return null;
    		
    }

    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return 
	 */        
    public String deleteAccumuloRow(String tableName, Text colFam, Text colQual, Text visibility) {
    		return null;
    }
    
    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param visibility
	 * @param numberOfThreads
	 * @return 
	 */       
    public JSONObject enumerateTable(String tableName, String visibility, String numberOfThreads) {
    		return null;
    }

    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return 
	 */  
    public JSONObject readRow(String tableName, Text colFam, Text colQual, Text visibility) {
    		return null;
    }
}

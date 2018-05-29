package org.renci.comet.accumuloops;

import org.apache.accumulo.core.cli.ScannerOpts;
import org.apache.accumulo.core.client.AccumuloException;

import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchDeleter;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Durability;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


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
    		ColumnVisibility colVis = new ColumnVisibility(visibility);
		//BatchWriter bw = conn.createBatchWriter(tableName,1000000, 60000, 2);
    		BatchWriterConfig cfg = new BatchWriterConfig();
    		cfg.setMaxMemory(10000000L);
    		cfg.setDurability(Durability.NONE);
    		BatchWriter bw = conn.createBatchWriter(tableName, cfg);
    		Mutation mutation = new Mutation(rowID);
    		mutation.put(colFam, colQual, colVis, value);
    		bw.addMutation(mutation);
    		bw.flush();
    		bw.close();
    		try {
			output.put(SUCCESS, "success, added Accumulo row: " + rowID + " to table: " + tableName);
		} catch (JSONException e1) {
			log.error("JSON Exception: " + e1.getMessage());
		}
    		return output;
    }

    public static JSONObject modifyRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Value value, Text visibility) {
		JSONObject output=new JSONObject();
		//BatchWriter bw = null;
		Mutation mut1 = new Mutation(rowID);
		//Text colFam2 = new Text(colFam);
		//Text ColFam2ColQual1 = new Text(colQual);
		if(visibility!=null) {
			mut1.put(colFam, colQual, new ColumnVisibility(visibility), value);
		} else {
			mut1.put(colFam, colQual, value);
		}
		try {
			BatchWriterConfig cfg = new BatchWriterConfig();
	    		cfg.setMaxMemory(10000000L);
	    		cfg.setDurability(Durability.NONE);
	    		BatchWriter bw = conn.createBatchWriter(tableName, cfg);
	    		bw.addMutation(mut1);
			bw.flush();
    			bw.close();
			/*bw = createBatchWriter(conn, tableName);
			bw.addMutation(mut1);
			bw.flush();
    			bw.close(); // flushes and release ---no need for bw.flush()
*/		} catch (Exception e) {
			System.out.println("Failed mutation in updating  entry (" + rowID + ")");

		}

		try {
			output.put("contextID", rowID);
			output.put(colQual.toString(), value);
		} catch (JSONException e) {
			System.out.println("JSON Exception: " + e.getMessage());
		}
		return output;
    }

    private static BatchWriter createBatchWriter(Connector conn, String table) {
		BatchWriter bw = null;

		BatchWriterConfig bwConfig = new BatchWriterConfig();
		// bwConfig.setMaxMemory(memBuffer);
		bwConfig.setTimeout(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		// bwConfig.setMaxWriteThreads(numberOfThreads);
		try {
			bw = conn.createBatchWriter(table, bwConfig);
		} catch (TableNotFoundException e) {
			System.out.println("Unable to find table " + table
					+ " to create batchWriter.");

		}

		return bw;
    }

    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return
     * @throws TableNotFoundException
     * @throws MutationsRejectedException
	 */
    public Map<String, Value> deleteAccumuloRow(Connector conn, Scanner scanner, String tableName, Text rowID, Text colFam, Text colQual, Text visibility) throws TableNotFoundException, MutationsRejectedException {
    		Map<String, Value> output = new HashMap<>();
    		Authorizations auths = new Authorizations(visibility.toString());
		BatchDeleter deleter= conn.createBatchDeleter(tableName, auths, 1, new BatchWriterConfig());
		Collection<Range> ranges = new ArrayList<Range>();
		Scanner tableScannerRange= conn.createScanner(tableName, auths);
		tableScannerRange.setRange(Range.exact(rowID));
		for (Entry<Key, Value> entry : tableScannerRange) {
            ranges.add(new Range(entry.getKey().getRow()));
            output.put(entry.getKey().toString(), entry.getValue());
		}
		deleter.setRanges(ranges);
		deleter.delete();
		deleter.close();


    		/*Mutation deleter = null;
		for (Entry<Key, Value> entry : scanner) {
			if (deleter == null) {
				deleter = new Mutation(entry.getKey().getRow());

			}
			if (visibility != null) {
				deleter.putDelete(new Text(colFam), new Text(colFam), new ColumnVisibility(visibility));
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
		}*/
		return output;
    }



	public Map<String, Value> enumerateRows(Connector conn, String tableName, Text rowID, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
		Map<String, Value> output = new HashMap<>();
		//ScannerOpts scanOpts = new ScannerOpts();
		// Create a scanner
		Authorizations auths = new Authorizations(visibility);
		conn.securityOperations().changeUserAuthorizations("root", auths);
	    Scanner scanner = conn.createScanner(tableName, auths);
	    //scanner.setBatchSize(scanOpts.scanBatchSize);
	    // Say start key is the one with key of row
	    // and end key is the one that immediately follows the row
	    scanner.setRange(new Range(rowID));
	    //scanner.fetchColumn(new Text("fam1"), new Text("k1"));
	    for (Map.Entry<Key, Value> entry : scanner) {
	    		String key =  entry.getKey().getRow() + " " + entry.getKey().getColumnFamily() + ":" + entry.getKey().getColumnQualifier();
			Value value = entry.getValue();
			output.put(key, value);

	    		System.out.printf("Key : %-50s  Value : %s\n", entry.getKey(), entry.getValue());
	    }
	    scanner.close();
	    return output;
	}

/*    *//**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param visibility
	 * @param numberOfThreads
	 * @return
     * @throws TableNotFoundException
	 *//*
    public Map<String, Value> enumerateTable(Connector conn, String tableName, String visibility, String numberOfThreads) throws TableNotFoundException {
    		Map<String, Value> output = new HashMap<String, Value>();

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
    			scan.close();
    		} catch (TableNotFoundException e) {
    			log.error("enumerateTable failed due to: " + e.getMessage());
    		} catch (Exception e) {
    			log.error("enumerateTable failed due to: " + e.getMessage());
    		}

    		return output;
    }*/

    /**
	 * Read single row with specific visibility.
	 * @param conn
	 * @param tableName
	 * @param rowID
	 * @return
     * @throws TableNotFoundException
     * @throws AccumuloException
     * @throws AccumuloSecurityException
	 */
    public Map<String, Value> readOneRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
    		Map<String, Value> output = new HashMap<>();
    		//ScannerOpts scanOpts = new ScannerOpts();
    		// Create a scanner
    		Authorizations auths = new Authorizations(visibility);
    		conn.securityOperations().changeUserAuthorizations("root", auths);
        Scanner scanner = conn.createScanner(tableName, auths);
        //scanner.setBatchSize(scanOpts.scanBatchSize);
        // Say start key is the one with key of row
        // and end key is the one that immediately follows the row
        scanner.setRange(new Range(rowID));
        scanner.fetchColumn(colFam, colQual);
        for (Map.Entry<Key, Value> entry : scanner) {
        		String key =  entry.getKey().getRow() + " " + entry.getKey().getColumnFamily() + ":" + entry.getKey().getColumnQualifier();
			Value value = entry.getValue();
			output.put(key, value);

        		System.out.printf("Key : %-50s  Value : %s\n", entry.getKey(), entry.getValue());
        }
        scanner.close();
        return output;
    }

}

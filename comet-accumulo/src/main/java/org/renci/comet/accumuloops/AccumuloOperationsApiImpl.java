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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.renci.comet.CometOps;


public class AccumuloOperationsApiImpl implements AccumuloOperationsApiIfce {
    public static final String ERROR = "error";
    public static final String SUCCESS = "Success";

    public static final int INDEX_ROW = 0;
    public static final int INDEX_FAMILY = 1;
    public static final int INDEX_KEY = 2;
    public static final int INDEX_TIMESTAMP = 3;
    public static final int INDEX_MAX = 4;
    
    private static final Logger log = Logger.getLogger(AccumuloOperationsApiImpl.class);
    private static String user;
    private int retryDuration;

    /**
     * Constructor
     * @param username username to access accumulo
     */
    public AccumuloOperationsApiImpl(String username) {
        super();
        user = username;
        retryDuration = CometOps.getInstance().retryDuration;
    }

    /**
     * Create unified JSONObject
     * @param messageType messageType
     * @param message message
     * @return JSONObject
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
     * @param conn conn
     * @param tableName tableName
     * @return JSONObject
     * @throws TableExistsException if table already exists
     * @throws AccumuloSecurityException in case of security error
     * @throws AccumuloException in case of accumulo error
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
     * @param conn conn
     * @param tableName tableName
     * @return JSONObject
     * @throws TableNotFoundException table not found
     * @throws AccumuloSecurityException in case of security error
     * @throws AccumuloException in case of accumulo error
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
     * @param conn conn
     * @param tableName tableName
     * @param rowID rowID
     * @param colFam colFam
     * @param colQual colQual
     * @param value value
     * @param visibility visibility
     * @return JSONObject
     * @throws TableNotFoundException table not found
     * @throws MutationsRejectedException change rejected
     */
    public JSONObject addAccumuloRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Value value, Text visibility) throws TableNotFoundException, MutationsRejectedException {
        JSONObject output = new JSONObject();
        ColumnVisibility colVis = new ColumnVisibility(visibility);
        BatchWriterConfig cfg = new BatchWriterConfig();
        BatchWriter bw = conn.createBatchWriter(tableName, cfg);
        Mutation mutation = new Mutation(rowID);
        mutation.put(colFam, colQual, colVis, value);
        bw.addMutation(mutation);
        bw.flush();
        bw.close();
        try {
            log.warn("Added to Accumulo " + visibility + "/" + rowID + "/" + colFam + "/" + colQual);
            output.put(SUCCESS, "success, added Accumulo row: " + rowID + " to table: " + tableName);
        } catch (JSONException e1) {
            log.error("JSON Exception: " + e1.getMessage());
        }
        return output;
    }

    public static JSONObject modifyRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Value value, Text visibility) {
        JSONObject output=new JSONObject();
        Mutation mut1 = new Mutation(rowID);
        if(visibility!=null) {
            mut1.put(colFam, colQual, new ColumnVisibility(visibility), value);
        } else {
            mut1.put(colFam, colQual, value);
        }
        try {
            BatchWriterConfig cfg = new BatchWriterConfig();
            BatchWriter bw = conn.createBatchWriter(tableName, cfg);
            bw.addMutation(mut1);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.error("Failed mutation in updating  entry (" + rowID + ")");

        }

        try {
            output.put("contextID", rowID);
            output.put(colQual.toString(), value);
        } catch (JSONException e) {
            log.error("JSON Exception: " + e.getMessage());
        }
        return output;
    }

    private static BatchWriter createBatchWriter(Connector conn, String table) {
        BatchWriter bw = null;

        BatchWriterConfig bwConfig = new BatchWriterConfig();
        bwConfig.setTimeout(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        try {
            bw = conn.createBatchWriter(table, bwConfig);
        } catch (TableNotFoundException e) {
            log.error("Unable to find table " + table
                    + " to create batchWriter.");

        }

        return bw;
    }

    /**
     * Create new Accumulo table.
     * @param conn conn
     * @param tableName tableName
     * @param rowID rowID
     * @param colFam colFam
     * @param colQual colQual
     * @param visibility visibility
     * @return row
     * @throws TableNotFoundException table not found
     * @throws MutationsRejectedException change rejected
     */
    public Map<String, Value> deleteAccumuloRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Text visibility) throws TableNotFoundException, MutationsRejectedException, AccumuloException, AccumuloSecurityException {
        Map<String, Value> output = new HashMap<>();
        BatchDeleter deleter = null;
        int count = 0;
        boolean succeed = false;
        long beginTime = System.currentTimeMillis();
        while(!succeed) {
            try {
                Authorizations auths = new Authorizations(visibility.toString());
                conn.securityOperations().changeUserAuthorizations(user, auths);
                deleter = conn.createBatchDeleter(tableName, auths, 1, new BatchWriterConfig());
                deleter.setRanges(Arrays.asList(Range.exact(rowID, colFam, colQual)));
                deleter.delete();
                succeed = true;
            }
            catch (Exception e) {
                log.error("Exception catched: " + e.toString());
                if (e.getMessage() != null && (e.getMessage().contains("BAD_AUTHORIZATIONS"))) {
                    if (visibility.equals(conn.securityOperations().getUserAuthorizations(user).toString())
                       && (System.currentTimeMillis() <= (beginTime + retryDuration)))
                    {
                        //zookeepers might not fully propagate yet, give it a retry
                        count++;
                        log.error("retry(" + count + "): " + visibility + "/" + rowID.toString());
                        continue;
                    }
                    log.error("authorizaions request: " + visibility + "/" + rowID.toString());
                    log.error("authorizaions actual: " + conn.securityOperations().getUserAuthorizations(user).toString());
                }
                throw e;
            } finally {
                if(deleter != null) {
                    deleter.close();
                }
            }
        }
        return output;
    }



    public Map<String[], Value> enumerateRows(Connector conn, String tableName, Text rowID, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
        Map<String[], Value> output = new HashMap<>();
        Scanner scanner = null;
        int count = 0;
        boolean succeed = false;
        long beginTime = System.currentTimeMillis();
        while(!succeed) {
            try {
                //ScannerOpts scanOpts = new ScannerOpts();
                // Create a scanner
                Authorizations auths = new Authorizations(visibility);
                conn.securityOperations().changeUserAuthorizations(user, auths);
                scanner = conn.createScanner(tableName, auths);
                //scanner.setBatchSize(scanOpts.scanBatchSize);
                // Say start key is the one with key of row
                // and end key is the one that immediately follows the row
                scanner.setRange(new Range(rowID));

                for (Map.Entry<Key, Value> entry : scanner) {
                    //String[] key format: {Row, ColFam, ColQual, Accumulo Timestamp}
                    String[] keys = new String[INDEX_MAX];
                    keys[INDEX_ROW] = entry.getKey().getRow().toString();
                    keys[INDEX_FAMILY] = entry.getKey().getColumnFamily().toString();
                    keys[INDEX_KEY] = entry.getKey().getColumnQualifier().toString();
                    keys[INDEX_TIMESTAMP] = Long.toString(entry.getKey().getTimestamp());
                    Value value = entry.getValue();
                    output.put(keys, value);
                }
                if (output.size() == 0 )
                {
                    log.info("Output empty:" + visibility + "/" + rowID.toString());
                }
                succeed = true;
                if (count > 0) {
                    log.warn("retry(" + count + ") succeed after " + (System.currentTimeMillis()- beginTime) + "(ms)");
                }
            }
            catch (Exception e) {
                log.error("Exception catched: " + e.toString());
                if (e.getMessage() != null && (e.getMessage().contains("BAD_AUTHORIZATIONS"))) {
                    if (visibility.equals(conn.securityOperations().getUserAuthorizations(user).toString())
                       && (System.currentTimeMillis() <= (beginTime + retryDuration)))
                    {
                        //zookeepers might not fully propagate yet, give it a retry
                        count++;
                        log.error("retry(" + count + "): " + visibility + "/" + rowID.toString());
                        continue;
                    }
                    log.error("BAD_AUTHORIZATIONS " + visibility + "/" + rowID.toString());
                    log.error("authorizaions request: " + visibility);
                    log.error("authorizaions actual: " + conn.securityOperations().getUserAuthorizations(user).toString());
                }
                throw e;
            } finally {
                if(scanner != null) {
                    scanner.close();
                }
            }
        }
        return output;
    }

    /**
     * Read single row with specific visibility.
     * @param conn conn
     * @param tableName tableName
     * @param rowID rowID
     * @param colFam colFam
     * @param colQual colQual
     * @param visibility visibility
     * @return row
     * @throws TableNotFoundException table not found
     * @throws AccumuloException in case of accumulo error
     */
    public Map<String[], Value> readOneRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
        Map<String[], Value> output = new HashMap<>();
        Scanner scanner = null;
        int count = 0;
        boolean succeed = false;
        long beginTime = System.currentTimeMillis();
        while(!succeed) {
            try {

                //ScannerOpts scanOpts = new ScannerOpts();
                // Create a scanner
                Authorizations auths = new Authorizations(visibility);
                conn.securityOperations().changeUserAuthorizations(user, auths);
                scanner = conn.createScanner(tableName, auths);
                //scanner.setBatchSize(scanOpts.scanBatchSize);
                // Say start key is the one with key of row
                // and end key is the one that immediately follows the row
                scanner.setRange(new Range(rowID));
                scanner.fetchColumn(colFam, colQual);
                for (Map.Entry<Key, Value> entry : scanner) {
                    String[] keys = new String[INDEX_MAX];
                    keys[INDEX_ROW] = entry.getKey().getRow().toString();
                    keys[INDEX_FAMILY] = entry.getKey().getColumnFamily().toString();
                    keys[INDEX_KEY] = entry.getKey().getColumnQualifier().toString();
                    keys[INDEX_TIMESTAMP] = Long.toString(entry.getKey().getTimestamp());
                    Value value = entry.getValue();
                    output.put(keys, value);
                }
                if (output.size() == 0 )
                {
                    log.info("Output empty:" + visibility + "/" + rowID.toString() + "/" + colFam.toString() + "/" + colQual.toString());
                }
                succeed = true;
                if (count > 0) {
                    log.warn("retry(" + count + ") succeed after " + (System.currentTimeMillis()- beginTime) + "(ms)");
                }
            }
            catch (Exception e) {
                log.error("Exception catched: " + e.toString());
                if (e.getMessage() != null && (e.getMessage().contains("BAD_AUTHORIZATIONS"))) {
                    if (visibility.equals(conn.securityOperations().getUserAuthorizations(user).toString())
                        && (System.currentTimeMillis() <= (beginTime + retryDuration)))
                    {
                        //zookeepers might not fully propagate yet, give it a retry
                        count++;
                        log.error("retry(" + count + "): " + visibility + "/" + rowID.toString() + "/" + colFam.toString() + "/" + colQual.toString());
                        continue;
                    }
                    log.error("BAD_AUTHORIZATIONS " + visibility + "/" + rowID.toString() + "/" + colFam.toString() + "/" + colQual.toString());
                    log.error("authorizaions request: " + visibility);
                    log.error("authorizaions actual: " + conn.securityOperations().getUserAuthorizations(user).toString());
                }
                throw e;
            } finally {
                if(scanner != null) {
                    scanner.close();
                }
            }
        }
        return output;
    }


    /**
     * Read single row with specific visibility return accumulo format.
     * @param conn conn
     * @param tableName tableName
     * @param rowID rowID
     * @param colFam colFam
     * @param colQual colQual
     * @param visibility visibility
     * @return row
     * @throws TableNotFoundException table not found
     * @throws AccumuloException in case of accumulo error
     * @throws AccumuloSecurityException in case of security error
     */
    public Map<String, Value> readOneRowAccuFormat(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
        Map<String, Value> output = new HashMap<>();
        Scanner scanner = null;
        int count = 0;
        boolean succeed = false;
        long beginTime = System.currentTimeMillis();
        while(!succeed) {
            try {
                //ScannerOpts scanOpts = new ScannerOpts();
                // Create a scanner
                Authorizations auths = new Authorizations(visibility);
                conn.securityOperations().changeUserAuthorizations(user, auths);
                scanner = conn.createScanner(tableName, auths);
                //scanner.setBatchSize(scanOpts.scanBatchSize);
                // Say start key is the one with key of row
                // and end key is the one that immediately follows the row
                scanner.setRange(new Range(rowID));
                scanner.fetchColumn(colFam, colQual);

                for (Map.Entry<Key, Value> entry : scanner) {
                    String key = entry.getKey().getRow() + " " + entry.getKey().getColumnFamily() + ":" + entry.getKey().getColumnQualifier();
                    Value value = entry.getValue();
                    output.put(key, value);
                }
                if (output.size() == 0 )
                {
                    log.info("Output empty:" + visibility + "/" + rowID.toString() + "/" + colFam.toString() + "/" + colQual.toString());
                }
                succeed = true;
                if (count > 0) {
                    log.warn("retry(" + count + ") succeed after " + (System.currentTimeMillis()- beginTime) + "(ms)");
                }
            }
            catch (Exception e) {
                log.error("Exception catched: " + e.toString());
                if (e.getMessage() != null && (e.getMessage().contains("BAD_AUTHORIZATIONS"))) {
                    if (visibility.equals(conn.securityOperations().getUserAuthorizations(user).toString())
                        && (System.currentTimeMillis() <= (beginTime + retryDuration)))
                    {
                        //zookeepers might not fully propagate yet, give it a retry
                        count++;
                        log.error("retry(" + count + "): " + visibility + "/" + rowID.toString() + "/" + colFam.toString() + "/" + colQual.toString());
                        continue;
                    }
                    log.error("BAD_AUTHORIZATIONS " + visibility + "/" + rowID.toString() + "/" + colFam.toString() + "/" + colQual.toString());
                    log.error("authorizaions request: " + visibility);
                    log.error("authorizaions actual: " + conn.securityOperations().getUserAuthorizations(user).toString());
                }
                throw e;
            } finally {
                if(scanner != null) {
                    scanner.close();
                }
            }
        }
        return output;
    }
}

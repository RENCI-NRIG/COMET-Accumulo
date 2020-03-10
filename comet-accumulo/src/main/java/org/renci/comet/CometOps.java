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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.apache.hadoop.io.Text;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Date;
import org.renci.comet.accumuloops.AccumuloOperationsApiIfce;
import org.renci.comet.accumuloops.AccumuloOperationsApiImpl;
import java.util.concurrent.locks.ReentrantLock;

public class CometOps implements CometOpsIfce {
    String checkTokenStrength;
    String instanceName;
    String zooServers;// Provide list of zookeeper server here. For example, localhost:2181
    String userName;
    String password;
    public String tableName; //provide Accumulo table name
    public String checkCert;
    public int retryDuration; // in milliseconds, this will be used by class AccumuloOperationsApiImpl() when retry
    private long expiredTime; // in milliseconds, this will be used to do interal write (user unaware) to avoid by accumulo age-off setting
    
    public static final String ERROR = "error";
    public static final String SUCCESS = "Success";

    /* we put following fields into accumulo <value> */
    public static final int FIELD_ISDELETED = 0;
    public static final int FIELD_WRITETOKEN = 1;
    public static final int FIELD_SCOPEVAUE = 2;
    public static final int FIELD_COMETVERSION = 3;
    public static final int FIELD_DELTIMESTAMP = 4;
    public static final int FIELD_WRITETIMESTAMP = 5;

    /* following are the fields in key (string[]) of entry read/enumerated from AccumuloOperationsApiImpl */
    public static final int INDEX_ROW = AccumuloOperationsApiImpl.INDEX_ROW;
    public static final int INDEX_FAMILY = AccumuloOperationsApiImpl.INDEX_FAMILY;
    public static final int INDEX_KEY = AccumuloOperationsApiImpl.INDEX_KEY;
    public static final int INDEX_TIMESTAMP = AccumuloOperationsApiImpl.INDEX_TIMESTAMP;

    private static final Logger log = Logger.getLogger(CometOps.class);

    private static final ReentrantLock fairLock = new ReentrantLock(true);
    private static final CometOps cometOpsInstance = new CometOps();
    private CometOps(){
        String[] properties = readProperties();
        checkTokenStrength = properties[0];
        checkCert = properties[1];
        instanceName = properties[2];
        zooServers = properties[3]; // Provide list of zookeeper server here. For example, localhost:2181
        userName = properties[4]; // Provide username
        password = properties[5]; // Provide password
        tableName = properties[6]; //provide Accumulo table name
        retryDuration = Integer.parseInt(properties[7]);
        expiredTime = Long.parseLong(properties[8]); //change to ms to compare with timestamp
        log.warn("retryDuration = " + retryDuration);
        log.warn("expiredTime = " + expiredTime);
        if (expiredTime != 0){
            try {
                Instance inst = new ZooKeeperInstance(instanceName, zooServers);
                Connector conn = inst.getConnector(userName, new PasswordToken(password));
                conn.tableOperations().setProperty(tableName, "table.iterator.scan.ageoff.opt.ttl", Long.toString(expiredTime * 2));
                conn.tableOperations().setProperty(tableName, "table.iterator.majc.ageoff.opt.ttl", Long.toString(expiredTime * 2));
                conn.tableOperations().setProperty(tableName, "table.iterator.minc.ageoff.opt.ttl", Long.toString(expiredTime * 2));
                log.warn("ageoff set to " + (expiredTime * 2));
            }
            catch(Exception e) {
                log.error(e);
                log.error("ageoff config failed - check with accumulo admin to see if you have permission" );
                System.exit(1);
            }
        }
    }

    public static CometOps getInstance() {
        return cometOpsInstance;
    }


    public static String[] readProperties() {
        String checkTokenStrength = "true";
        String checkClientCert = "true";
        String instanceName = "aws-development";
        String zooServers = "zoo1,zoo2,zoo3"; // Provide list of zookeeper server here. For example, localhost:2181
        String userName = "root"; // Provide username
        String password = "secret"; // Provide password
        String tableName="trace"; //provide Accumulo table name
        String retryDuration = "1000";
        String expiredTime = "0";

            Properties prop = new Properties();
            InputStream input = null;

            Map<String, String> env_map = System.getenv();

            if(env_map.get("COMET_CHECK_TOKEN_STRENGTH") != null) {
                checkTokenStrength = env_map.get("COMET_CHECK_TOKEN_STRENGTH");
            }
            if(env_map.get("COMET_CHECK_CLIENT_CERT") != null) {
                checkClientCert = env_map.get("COMET_CHECK_CLIENT_CERT");
            }
            if(env_map.get("ACCUMULO_INSTANCE") != null) {
                instanceName = env_map.get("ACCUMULO_INSTANCE");
            }
            if(env_map.get("ACCUMULO_ZOOSERVERS") != null) {
                zooServers = env_map.get("ACCUMULO_ZOOSERVERS");
            }
            if(env_map.get("ACCUMULO_USER") != null) {
                userName = env_map.get("ACCUMULO_USER");
            }
            if(env_map.get("ACCUMULO_PASSWORD") != null) {
                password = env_map.get("ACCUMULO_PASSWORD");
            }
            if(env_map.get("ACCUMULO_TABLENAME") != null) {
                tableName = env_map.get("ACCUMULO_TABLENAME");
            }
            if(env_map.get("COMET_RETRY_DURATION") != null) {
                retryDuration = env_map.get("COMET_RETRY_DURATION");
            }
            if(env_map.get("COMET_RECORD_EXPIRE_TIME") != null) {
                expiredTime = env_map.get("COMET_RECORD_EXPIRE_TIME");
            }

            try {
                input = CometOps.class.getClassLoader().getResourceAsStream("application.properties");

                // load a properties file
                prop.load(input);

                log.debug("server.ssl.key-store=" + prop.getProperty("server.ssl.key-store")); 
                log.debug("server.ssl.key-store-password=" + prop.getProperty("server.ssl.key-store-password")); 
                log.debug("server.ssl.key-password=" + prop.getProperty("server.ssl.key-password")); 
                log.debug("server.ssl.trust-store=" + prop.getProperty("server.ssl.trust-store")); 
                log.debug("server.ssl.trust-store-password=" + prop.getProperty("server.ssl.trust-store-password")); 

                if(env_map.get("COMET_CHECK_TOKEN_STRENGTH") == null) {
                    checkTokenStrength = prop.getProperty("comet.checkStrength");
                }
                if(env_map.get("COMET_CHECK_CLIENT_CERT") == null) {
                    checkClientCert = prop.getProperty("comet.certCheck");
                }
                if(env_map.get("ACCUMULO_INSTANCE") == null) {
                    instanceName = prop.getProperty("accumulo.instanceName");
                }
                if(env_map.get("ACCUMULO_ZOOSERVERS") == null) {
                    zooServers = prop.getProperty("accumulo.zooServers");
                }
                if(env_map.get("ACCUMULO_USER") == null) {
                    userName = prop.getProperty("accumulo.userName");
                }
                if(env_map.get("ACCUMULO_PASSWORD") == null) {
                    password = prop.getProperty("accumulo.password");
                }
                if(env_map.get("ACCUMULO_TABLENAME") == null) {
                    tableName = prop.getProperty("accumulo.tableName");
                }
                if(env_map.get("COMET_RETRY_DURATION") == null) {
                    retryDuration = prop.getProperty("comet.retryDuration");
                }

                /* log.debug("checkTokenStrength: " + checkTokenStrength
                        + ", checkClientCert: " + checkClientCert
                        + ", instanceName: " +  instanceName
                        + ", zooServers: " +  zooServers
                        + ", userName: " +  userName
                        + ", password: " +  password
                        + ", tableName: " +  tableName); */
                return new String[] {checkTokenStrength, checkClientCert, instanceName, zooServers, userName, password, tableName, retryDuration, expiredTime};
    
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("Some properties are missing in the application.properties file, using default values."); 
            return new String[] {checkTokenStrength, checkClientCert, instanceName, zooServers, userName, password, tableName, retryDuration, expiredTime};
    }
    
    /**
     * Check if password contains:
     * At least 8 chars
     * Contains at least one digit
     * Contains at least one lower alpha char and one upper alpha char
     * Does not contain space, tab, etc.
     * 
     * ^                 # start-of-string
     * (?=.*[0-9])       # a digit must occur at least once
     * (?=.*[a-z])       # a lower case letter must occur at least once
     * (?=.*[A-Z])       # an upper case letter must occur at least once
     * (?=\S+$)          # no whitespace allowed in the entire string
     * .{8,}             # anything, at least eight places though
     * $                 # end-of-string
     * 
     * @param expression string
     * @return true on success and false otherwise
     */
    public static boolean isTokenStrong(String expression) {
        String checkTokenStrength = readProperties()[0];
        //token strength checking turned off. Always return true.
        if (checkTokenStrength.equals("false"))
            return true;
        
        //Token strength checking.
        if (expression != null) {
            return expression.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
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

    public JSONObject writeScope (String contextID, String family, String key, String scopeValue, String readToken, String writeToken) 
                                throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException {
        Instance inst = new ZooKeeperInstance(instanceName, zooServers);
        Connector conn = inst.getConnector(userName, new PasswordToken(password));
        AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl(userName);
        JSONObject jsonOutput = null;

        Text rowID = new Text(contextID);
        Text colFam = new Text(family);
        Text colQual = new Text(key);
        Text vis = new Text(readToken);

        Map<String, Value> mapOutput = new HashMap<String, Value>();
        fairLock.lock();
        try {
            mapOutput = accu.readOneRowAccuFormat(conn, tableName, rowID, colFam, colQual, readToken);
            int mapSize = mapOutput.size();
            if (mapSize == 0) {
                log.info("No scope, creating new one.");
            } else if (mapSize != 1) {
                log.error("mapSize != 1 - Conflict in Accumulo record, please clean up.");
            } else {
                for (Map.Entry<String, Value> entry : mapOutput.entrySet()) {
                    Value v = entry.getValue();
                    log.info("got Value: " + v + "from readOneRowAccuFormat()");
                    String[] deserialized = null;
                    try {
                        deserialized = (String[]) deserialize(v.get());
                    } catch (ClassNotFoundException | IOException e1) {
                        log.error("deserialization failed: " + e1);
                    }
                    if (deserialized != null && deserialized.length > FIELD_DELTIMESTAMP) {
                        String debugString = "";
                        for (String s : deserialized)
                            debugString = debugString + s + " ,";
                        log.info("deserialized values:" + debugString);
                        if (deserialized[FIELD_ISDELETED].equals("true")) {
                            try {
                                JSONObject output = new JSONObject();
                                output.put(ERROR, "Failed to write scope: scope already deleted.");
                                log.error("Failed to write scope: scope already deleted.");
                                return output;
                            } catch (JSONException e) {
                                log.error("JSONException: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (deserialized == null) {
                        log.warn("deserialized values == null");
                    }
                    else {
                        log.error("deserialized.length: " + deserialized.length);
                        for (String s : deserialized)
                            log.info(s);
                    }
                }
            }

            //Accumulo value field format: {ifDeleted, writeToken, scopeValue, Comet_version, deletionTimeStamp, userWriteTimeStamp}
            long userWriteTimeStamp = System.currentTimeMillis();
            String[] value = {"false", writeToken, scopeValue, CometInitializer.COMET_VERSION, "", Long.toString(userWriteTimeStamp)};
            log.info("Writing in scope: contextID: " + contextID + "\n family: " + family + "\n key: " + key + "\n readToken: " + readToken);

            byte[] serializedByteArray = null;
            try {
                serializedByteArray = serialize(value);
                log.debug("Serialize successful!");
            } catch (IOException e) {
                log.error(e.toString());
            }

            org.apache.accumulo.core.data.Value accumuloValue = new org.apache.accumulo.core.data.Value(serializedByteArray);
            log.debug("Writing to Accumulo done!");
            jsonOutput = accu.addAccumuloRow(conn, tableName, rowID, colFam, colQual, accumuloValue, vis);
        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().contains("BAD_AUTHORIZATIONS"))) {
                log.error("CometOps:writeScope:Intentionally ignoring the Exception; assuming row does not exist");
                e.printStackTrace();
            } else {
                throw e;
            }
        } finally {
            fairLock.unlock();
        }
        return jsonOutput;
    }

    public JSONObject deleteScope (String contextID, String family, String key, String readToken, String writeToken) throws AccumuloException, AccumuloSecurityException, TableNotFoundException, JSONException {
        Instance inst = new ZooKeeperInstance(instanceName, zooServers);
        Connector conn = inst.getConnector(userName, new PasswordToken(password));
        AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl(userName);
        JSONObject jsonOutput = new JSONObject();

        Text rowID = new Text(contextID);
        Text colFam = new Text(family);
        Text colQual = new Text(key);
        Text vis = new Text(readToken);
        Map<String, Value> mapOutput = new HashMap<String, Value>();

        fairLock.lock();
        try {
            mapOutput = accu.readOneRowAccuFormat(conn, tableName, rowID, colFam, colQual, readToken);

            int mapSize = mapOutput.size();
            if (mapSize == 0) {
                log.error("Failed to delete scope: no such entry");
                jsonOutput.put(ERROR, "Failed to delete scope: no such entry");
                return jsonOutput;
            }

            if (mapSize != 1) {
                log.error("Failed to delete scope: conflict in Accumulo record, please clean up");
                jsonOutput.put(ERROR, "Failed to delete scope: conflict in Accumulo record, please clean up");
                return jsonOutput;
            }

            for (Map.Entry<String, Value> entry : mapOutput.entrySet()) {
                Value v = entry.getValue();
                String[] deserialized = null;
                try {
                    deserialized = (String[]) deserialize(v.get());
                } catch (ClassNotFoundException | IOException e1) {
                    log.error("deserialization failed: " + e1);
                    jsonOutput.put(ERROR, "Failed to delete scope: deserialization failed");
                }
                if (deserialized != null && deserialized.length > FIELD_DELTIMESTAMP) {
                    if (!writeToken.equals(deserialized[FIELD_WRITETOKEN])) {
                        try {
                            jsonOutput.put(ERROR, "Cannot delete scope: incorrect write token");
                        } catch (JSONException e) {
                            log.error("JSON Exception: " + e.getMessage());
                        }
                        log.error("Cannot delete scope: incorrect write token");
                        return jsonOutput;
                    }

                    try {
                        accu.deleteAccumuloRow(conn, tableName, rowID, colFam, colQual, vis);
                        log.info("Scope deleted, writeToken = " + writeToken);
                        try {
                            jsonOutput.put(SUCCESS, "Scope deleted");
                        } catch (JSONException e) {
                            log.error("JSON Exception: " + e.getMessage());
                        }
                    } catch (Exception e) {
                        log.error("Exception: " + e.getMessage());
                    }
                }
            }
        } finally {
            fairLock.unlock();
        }
        return jsonOutput;
    }

    /**
     * Comet will do a internal write to the data to update timestamp(ts) if ts is too old to avoid this data age-off by accumulo
     * @param entry the record entry
     * @param deserialized the value of the entry
     * @param conn conn
     * @param tableName tableName
     * @param vis readToken
     * @return None
     */
    private void updateAccuTimestamp(Map.Entry<String[], Value> entry, String[] deserialized, Connector conn, String tableName, Text vis) {
        try {
            Text rowID = new Text(entry.getKey()[INDEX_ROW]);
            Text colFam = new Text(entry.getKey()[INDEX_FAMILY]);
            Text colQual = new Text(entry.getKey()[INDEX_KEY]);
            String sAccuTimestamp = entry.getKey()[INDEX_TIMESTAMP];
            long accumuloTime = Long.parseLong(sAccuTimestamp);
            long currentTime = System.currentTimeMillis();
            
            if ((currentTime - accumuloTime) >= expiredTime ) {
                String userWriteTimeStamp = sAccuTimestamp;
                if (deserialized.length > FIELD_WRITETIMESTAMP){
                    userWriteTimeStamp = deserialized[FIELD_WRITETIMESTAMP];
                }
                Date userWriteDate = new Date(Long.parseLong(userWriteTimeStamp));
                log.info("CurrentTime: " + currentTime + "AccuTimestamp: " + accumuloTime + ", diff(ms) = " + (currentTime - accumuloTime) + " > " + expiredTime );
                log.info("userWriteTimeStamp: " + userWriteTimeStamp + ", userWriteDate: " + userWriteDate);

                //Accumulo value field format: {ifDeleted, writeToken, scopeValue, Comet_version, deletionTimeStamp, userWriteTimeStamp}
                String[] value = {"false", deserialized[FIELD_WRITETOKEN], deserialized[FIELD_SCOPEVAUE], CometInitializer.COMET_VERSION, "", userWriteTimeStamp};
                log.warn("internal write for ts update: " + vis +"/" + rowID + "/" + colFam + "/" + colQual);
                for (String s : value) {
                    log.debug("internal write value: " + s);
                }
                
                fairLock.lock();
                try {
                    AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl(userName);
                    byte[] serializedByteArray = null;
                    serializedByteArray = serialize(value);
                    org.apache.accumulo.core.data.Value accumuloValue = new org.apache.accumulo.core.data.Value(serializedByteArray);
                    accu.addAccumuloRow(conn, tableName, rowID, colFam, colQual, accumuloValue, vis);
                } catch (Exception e) {
                    log.error(e.toString());
                } finally {
                    fairLock.unlock();
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public JSONObject readScope (String contextID, String family, String key, String readToken) throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException {
        Instance inst = new ZooKeeperInstance(instanceName, zooServers);
        Connector conn = inst.getConnector(userName, new PasswordToken(password));
        AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl(userName);

        Text rowID = new Text(contextID);
        Text colFam = new Text(family);
        Text colQual = new Text(key);
        String scopeValue = null;
        Map<String[], Value> output = new HashMap<String[], Value>();

        JSONObject jsonOutput = new JSONObject();
        fairLock.lock();
        try {
            output = accu.readOneRow(conn, tableName, rowID, colFam, colQual, readToken);
        } finally {
            fairLock.unlock();
        }
        String debugString2 = output.size() + " record," + readToken + "/" + contextID + "/" + family + "/" + key;
        if (output.size() == 0) {
            log.warn(debugString2);
        }
        else{
            log.info(debugString2);
        }
        for (Map.Entry<String[], Value> entry : output.entrySet()) {
            Value v = entry.getValue();
            String[] deserialized = null;
            try {
                deserialized = (String[]) deserialize(v.get());
            } catch (ClassNotFoundException | IOException e1) {
                log.error("deserialization failed: " + e1);
            }
            if (deserialized != null && deserialized.length > FIELD_DELTIMESTAMP) {
                String debugString = "";
                for (String s : deserialized)
                    debugString = debugString + s + " ,";
                log.debug("deserialized values:" + debugString);
                if (deserialized[FIELD_ISDELETED].equals("true")) {
                    try {
                        jsonOutput.put(ERROR, "Failed to read scope: scope already deleted.");
                        log.warn("Scope already deleted: " + readToken + "/" + contextID + "/" + family + "/" + key);
                        return jsonOutput;
                    } catch (JSONException e) {
                        log.error("JSONException: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                try {
                    jsonOutput.put("contextId", entry.getKey()[INDEX_ROW]);
                    jsonOutput.put("family", entry.getKey()[INDEX_FAMILY]);
                    jsonOutput.put("key", entry.getKey()[INDEX_KEY]);
                    jsonOutput.put("value", deserialized[FIELD_SCOPEVAUE]);
                } catch (JSONException e) {
                    log.error("JSONException: " + e.getMessage());
                    e.printStackTrace();
                }

                if ( expiredTime != 0 ) {
                    updateAccuTimestamp(entry, deserialized, conn, tableName, new Text(readToken));
                }
            }
            else if (deserialized == null) {
                log.warn("deserialized values == null");
            }
            else {
                log.error("deserialized.length: " + deserialized.length);
                for (String s : deserialized)
                    log.info(s);
            }
        }
        return jsonOutput;
    }

    public JSONObject enumerateScopesWithFamily(String contextID, String family, String readToken) throws AccumuloException, AccumuloSecurityException {
        Instance inst = new ZooKeeperInstance(instanceName, zooServers);
        Connector conn = inst.getConnector(userName, new PasswordToken(password));
        AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl(userName);

        Text rowID = new Text(contextID);
        Map<String[], Value> output = new HashMap<String[], Value>();

        JSONObject jsonOutput = new JSONObject();

        fairLock.lock();
        try {
            output = accu.enumerateRows(conn, tableName, rowID, readToken);
        } catch (TableNotFoundException e2) {
            // TODO Auto-generated catch block
            log.error("Table not found: " + e2);
        } finally {
            fairLock.unlock();
        }

        try {
            jsonOutput.put("contextId", contextID);
            jsonOutput.put("family", family);
        } catch (JSONException e3) {
            e3.printStackTrace();
        }

        JSONArray jArr = new JSONArray();

        String debugString2 = output.size() + " record," + readToken + "/" + contextID + "/" + family;
        if (output.size() == 0) {
            log.warn(debugString2);
        }
        else{
            log.info(debugString2);
        }

        for (Map.Entry<String[], Value> entry : output.entrySet()) {
            JSONObject arrayElement = new JSONObject();
            Value v = entry.getValue();
            String[] deserialized = null;

            try {
                deserialized = (String[]) deserialize(v.get());
            } catch (ClassNotFoundException | IOException e1) {
                log.error("deserialization failed: " + e1);
            }

            if (deserialized != null && deserialized.length > FIELD_DELTIMESTAMP) {
                String debugString = "";
                for (String s : deserialized)
                    debugString = debugString + s + " ,";
                log.debug("deserialized values:" + debugString);
                if (deserialized[FIELD_ISDELETED].equals("false") && entry.getKey()[INDEX_FAMILY].equals(family)) {
                    try {
                        arrayElement.put("key", entry.getKey()[INDEX_KEY]);
                        arrayElement.put("value", deserialized[FIELD_SCOPEVAUE]);
                        jArr.put(arrayElement);
                    } catch (JSONException e) {
                        log.error("JSONException: " + e.getMessage());
                        e.printStackTrace();
                    }
                    if ( expiredTime != 0 ) {
                        updateAccuTimestamp(entry, deserialized, conn, tableName, new Text(readToken));
                    }
                }
            }
            else if (deserialized == null) {
                log.warn("deserialized values == null");
            }
            else {
                log.error("deserialized.length: " + deserialized.length);
                for (String s : deserialized)
                    log.debug(s);
            }
        }

        try {
            jsonOutput.put("entries", jArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonOutput;
    }

    public JSONObject enumerateScopes(String contextID, String readToken) throws AccumuloException, AccumuloSecurityException {
        Instance inst = new ZooKeeperInstance(instanceName, zooServers);
        Connector conn = inst.getConnector(userName, new PasswordToken(password));
        AccumuloOperationsApiImpl accu = new AccumuloOperationsApiImpl(userName);

        Text rowID = new Text(contextID);
        Map<String[], Value> output = new HashMap<String[], Value>();

        JSONObject jsonOutput = new JSONObject();

        fairLock.lock();
        try {
            output = accu.enumerateRows(conn, tableName, rowID, readToken);
        } catch (TableNotFoundException e2) {
            // TODO Auto-generated catch block
            log.error("Table not found: " + e2);
        } finally {
            fairLock.unlock();
        }

        try {
            jsonOutput.put("contextId", contextID);
        } catch (JSONException e3) {
            e3.printStackTrace();
        }

        JSONArray jArr = new JSONArray();

        String debugString2 = output.size() + " record," + readToken + "/" + contextID;
        if (output.size() == 0) {
            log.warn(debugString2);
        }
        else{
            log.info(debugString2);
        }
        for (Map.Entry<String[], Value> entry : output.entrySet()) {
            JSONObject arrayElement = new JSONObject();
            Value v = entry.getValue();
            String[] deserialized = null;

            try {
                deserialized = (String[]) deserialize(v.get());
            } catch (ClassNotFoundException | IOException e1) {
                log.error("deserialization failed: " + e1);
            }

            if (deserialized != null && deserialized.length > FIELD_DELTIMESTAMP) {
                String debugString = "";
                for (String s : deserialized)
                    debugString = debugString + s + " ,";
                log.debug("deserialized values:" + debugString);
                if (deserialized[FIELD_ISDELETED].equals("false")) {
                    try {
                        arrayElement.put("family", entry.getKey()[INDEX_FAMILY]);
                        arrayElement.put("key", entry.getKey()[INDEX_KEY]);
                        arrayElement.put("value", deserialized[FIELD_SCOPEVAUE]);
                        jArr.put(arrayElement);
                    } catch (JSONException e) {
                        log.error("JSONException: " + e.getMessage());
                        e.printStackTrace();
                    }
                    if ( expiredTime != 0 ) {
                        updateAccuTimestamp(entry, deserialized, conn, tableName, new Text(readToken));
                    }
                }
            }
        }

        try {
            jsonOutput.put("entries", jArr);
        } catch (JSONException e) {
            log.error("JSONException: " + e.getMessage());
            e.printStackTrace();
        }
        return jsonOutput;
    }
}

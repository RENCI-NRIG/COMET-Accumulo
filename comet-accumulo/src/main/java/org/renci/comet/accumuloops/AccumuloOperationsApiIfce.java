package org.renci.comet.accumuloops;

import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.security.Authorizations;

import java.util.Map;

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
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.log4j.Logger;
import org.apache.hadoop.io.Text;
import org.codehaus.jettison.json.JSONObject;


public interface AccumuloOperationsApiIfce {
/*	*//**
	 * Create new Zookeeper instance.
	 * @param instanceName
	 * @param zooServers
	 * @return Instance
	 *//*
    Instance createZooKeeperInstance(String instanceName, String zooServers);

    *//**
	 * Create new Accumulo connector.
	 * @param userName
	 * @param password
	 * @return AccuConnector
	 *//*
    Instance getAccumuloConnector(String userName, String password);*/

    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @return
     * @throws TableExistsException
     * @throws AccumuloSecurityException
     * @throws AccumuloException
	 */
    public JSONObject createAccumuloTable(Connector conn, String tableName) throws AccumuloException, AccumuloSecurityException, TableExistsException;

    /**
	 * Delete Accumulo table.
	 * @param userName
	 * @return
	 */
    public JSONObject deleteAccumuloTable(Connector conn, String tableName) throws TableNotFoundException, AccumuloException, AccumuloSecurityException;

    /**
	 * Create new Accumulo row.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param value
	 * @param visibility
	 * @return
	 */
    public JSONObject addAccumuloRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Value value, Text visibility) throws TableNotFoundException, MutationsRejectedException;

    /**
	 * Delete Accumulo row.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return
	 */
   public Map<String, Value> deleteAccumuloRow(Connector conn, Scanner scanner, String tableName, Text rowID, Text colFam, Text colQual, Text visibility) throws MutationsRejectedException, TableNotFoundException;

   /**
	 * Delete Accumulo row.
	 * @param conn
	 * @param tableName
	 * @param rowID
	 * @param visibility
	 * @throws TableNotFoundException
	 * @return
	 */
   public Map<String, Value> enumerateRows(Connector conn, String tableName, Text rowID, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException;

    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return
     * @throws AccumuloSecurityException
     * @throws AccumuloException
	 */
   public Map<String, Value> readOneRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, String visibility) throws TableNotFoundException, AccumuloException, AccumuloSecurityException;
}

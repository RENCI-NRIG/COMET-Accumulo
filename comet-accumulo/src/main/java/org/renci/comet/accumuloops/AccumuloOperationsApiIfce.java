package org.renci.comet.accumuloops;

import org.apache.accumulo.core.client.BatchScanner;
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


interface AccumuloOperationsApiIfce {
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
    JSONObject createAccumuloTable(Connector conn, String tableName) throws AccumuloException, AccumuloSecurityException, TableExistsException;
    
    /**
	 * Delete Accumulo table.
	 * @param userName
	 * @return 
	 */       
    JSONObject deleteAccumuloTable(Connector conn, String tableName) throws TableNotFoundException, AccumuloException, AccumuloSecurityException;
    
    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param value
	 * @param visibility
	 * @return 
	 */    
    JSONObject addAccumuloRow(Connector conn, String tableName, Text rowID, Text colFam, Text colQual, Value value, Text visibility) throws TableNotFoundException, MutationsRejectedException;

    /**
	 * Create new Accumulo table.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return 
	 */        
    JSONObject deleteAccumuloRow(Connector conn, Scanner scanner, String tableName, Text family, Text colFam, Text colQual, Text visibility) throws TableNotFoundException;    
    
    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param visibility
	 * @param numberOfThreads
	 * @return 
	 * @throws TableNotFoundException 
	 */       
    JSONObject enumerateTable(Connector conn, String tableName, String visibility, String numberOfThreads) throws TableNotFoundException;

    /**
	 * Enumerate table to get all rows with specific visibility.
	 * @param tableName
	 * @param colFam
	 * @param colQual
	 * @param visibility
	 * @return 
	 */  
     Scanner readRow(Connector conn, String tableName, Text rowID) throws TableNotFoundException;
}

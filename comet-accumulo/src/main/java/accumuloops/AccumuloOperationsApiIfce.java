package accumuloops;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
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
	/**
	 * Create new Zookeeper instance. 
	 * @param instanceName
	 * @param zooServers
	 * @return Instance
	 */
    Instance createZKInstance(String instanceName, String zooServers);
    
    /**
	 * Create new Accumulo connector. 
	 * @param userName
	 * @param password
	 * @return AccuConnector
	 */
    Instance createAccuConnector(String userName, String password);
    
    /**
	 * Create new Accumulo table.
	 * @param userName
	 * @param password
	 * @return 
	 */    
    JSONObject createAccuTable(String tableName);
}

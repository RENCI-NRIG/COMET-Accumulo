package org.renci.comet;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public interface CometOpsIfce {
	/**
	 * 
	 * @param contextID contextID
	 * @param family family
	 * @param key key
	 * @param value value
	 * @param readToken readToken
	 * @param writeToken writeToken
	 * @return JSONObject indicating if write operation is successful
	 * @throws AccumuloException in case of accumulo error
	 * @throws AccumuloSecurityException in case of security error
	 * @throws TableNotFoundException in case table not found
	 * @throws TableExistsException in case table already exists
	 */
    public JSONObject writeScope (String contextID, String family, String key, String value, String readToken, String writeToken)
    		throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException;

    /**
     * 
     * @param contextID contextID
     * @param family family
     * @param key key
     * @param readToken readToken
     * @param writeToken writeToken
     * @return JSONObject indicating if delete operation is successful
     * @throws AccumuloException in case of accumulo error
     * @throws AccumuloSecurityException in case of security error
     * @throws TableNotFoundException in case table not found
     * @throws JSONException in case table already exists
     */
    public JSONObject deleteScope(String contextID, String family, String key, String readToken, String writeToken)
    		throws AccumuloException, AccumuloSecurityException, TableNotFoundException, JSONException;

    /**
     * 
     * @param contextID contextID
     * @param family family
     * @param key key
     * @param readToken readToken
     * @return JSONObject with value
     * @throws AccumuloException in case of accumulo error
     * @throws AccumuloSecurityException in case of security error
     * @throws TableNotFoundException in case table not found
     * @throws TableExistsException in case table already exists
     */
    public JSONObject readScope (String contextID, String family, String key, String readToken)
    		throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException;

    /**
     * 
     * @param contextID contextID
     * @param readToken readToken
     * @return JSONObject with list of values
     * @throws AccumuloException in case of accumulo error
     * @throws AccumuloSecurityException in case of security error
     */
    public JSONObject enumerateScopes(String contextID, String readToken)
    		throws AccumuloException, AccumuloSecurityException;

    /**
     * 
     * @param contextID contextID
     * @param family family
     * @param readToken readToken
     * @return JSONObject with list of values
     * @throws AccumuloException in case of accumulo error
     * @throws AccumuloSecurityException in case of security error
     */
    public JSONObject enumerateScopesWithFamily(String contextID, String family, String readToken)
    		throws AccumuloException, AccumuloSecurityException;
}

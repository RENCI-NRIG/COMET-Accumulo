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
	 * @param contextID
	 * @param family
	 * @param key
	 * @param value
	 * @param readToken
	 * @param writeToken
	 * @return JSONObject indicating if write operation is successful
	 * @throws AccumuloException
	 * @throws AccumuloSecurityException
	 * @throws TableNotFoundException
	 * @throws TableExistsException
	 */
    public JSONObject writeScope (String contextID, String family, String key, String value, String readToken, String writeToken)
    		throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException;

    /**
     * 
     * @param contextID
     * @param family
     * @param key
     * @param readToken
     * @param writeToken
     * @return JSONObject indicating if delete operation is successful
     * @throws AccumuloException
     * @throws AccumuloSecurityException
     * @throws TableNotFoundException
     * @throws JSONException
     */
    public JSONObject deleteScope(String contextID, String family, String key, String readToken, String writeToken)
    		throws AccumuloException, AccumuloSecurityException, TableNotFoundException, JSONException;

    /**
     * 
     * @param contextID
     * @param family
     * @param key
     * @param readToken
     * @return JSONObject with value
     * @throws AccumuloException
     * @throws AccumuloSecurityException
     * @throws TableNotFoundException
     * @throws TableExistsException
     */
    public JSONObject readScope (String contextID, String family, String key, String readToken)
    		throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException;

    /**
     * 
     * @param contextID
     * @param readToken
     * @return JSONObject with list of values
     * @throws AccumuloException
     * @throws AccumuloSecurityException
     */
    public JSONObject enumerateScopes(String contextID, String readToken)
    		throws AccumuloException, AccumuloSecurityException;

    /**
     * 
     * @param contextID
     * @param family
     * @param readToken
     * @return JSONObject with list of values
     * @throws AccumuloException
     * @throws AccumuloSecurityException
     */
    public JSONObject enumerateScopesWithFamily(String contextID, String family, String readToken)
    		throws AccumuloException, AccumuloSecurityException;
}

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
    public JSONObject writeScope (String contextID, String family, String key, String value, String readToken, String writeToken)
    		throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException;

    public JSONObject deleteScope(String contextID, String family, String key, String readToken, String writeToken)
    		throws AccumuloException, AccumuloSecurityException, TableNotFoundException, JSONException;

    public JSONObject readScope (String contextID, String family, String key, String readToken)
    		throws AccumuloException, AccumuloSecurityException,TableNotFoundException, TableExistsException;

    public JSONObject enumerateScopes(String contextID, String family, String readToken)
    		throws AccumuloException, AccumuloSecurityException;

}

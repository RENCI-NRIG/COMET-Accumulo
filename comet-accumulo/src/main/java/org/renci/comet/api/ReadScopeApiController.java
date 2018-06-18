package org.renci.comet.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;
import org.codehaus.jettison.json.JSONObject;
import org.renci.comet.model.CometResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.renci.comet.CometInitializer;
import org.renci.comet.CometOps;

@javax.annotation.Generated(value = "org.renci.comet.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

@Controller
public class ReadScopeApiController implements ReadScopeApi {

    private static final Logger log = LoggerFactory.getLogger(ReadScopeApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    //private boolean certValid = false;

    @org.springframework.beans.factory.annotation.Autowired
    public ReadScopeApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }


    public ResponseEntity<CometResponse> readScopeGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "contextID", required = true) String contextID,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "family", required = true) String family,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Key", required = true) String key,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "readToken", required = true) String readToken) {
        String accept = request.getHeader("Accept");

        X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");

        //System.out.println("Read scope: request accepted");

        if (certs == null) {
			//System.out.print("ReadScope, Cert is NULL!!!\n");
		} else {
			log.debug("Got client certificate:");
			for (X509Certificate x : certs) {
				log.debug(x.toString());
			}
		}

        //ReadScope doesn't check client cert.
		/*if (certs != null) {
			try {
                for (int i = 0; i < certs.length; i++)
                    certs[i].checkValidity();
                	certValid = true;
            } catch (Exception e) {
            		log.error("Unable to validate certificate!");
			}
		}*/

		if (contextID == null || family == null || key == null || readToken == null) {
			try {
				return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"Invalid arguments\",  \"value\" : \"{}\",  \"version\" : \"version\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.BAD_REQUEST);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

        log.debug("ReadScope operation: contextID: " + contextID + "\n family: " + family + "\n key: " + key + "\n readToken: " + readToken);

		if (accept != null && accept.contains("application/json")) {
        		try {
        			CometOps cometOps = new CometOps();
        			//JSONObject test = cometOps.readScope("id0001", "hero", "name", "secretId");
        			//System.out.println(test);
        			JSONObject output = cometOps.readScope(contextID, family, key, readToken);
        			ObjectMapper objectMapper = new ObjectMapper();
        			CometResponse comet = new CometResponse();
        			comet.setValue(output.toString());
        			comet.setStatus("OK");
        			comet.setMessage("message");
        			comet.setVersion("0.1");
        			//System.out.println(comet.toString());
        			String crTemp = "{  \"message\" : \"success\",  \"value\" : " + output.toString() + ",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"OK\"}";
        			//System.out.println(crTemp);
        			//return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"message\",  \"value\" : \"{}\",  \"version\" : \"version\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.OK);
        			return new ResponseEntity<CometResponse>(objectMapper.readValue(crTemp, CometResponse.class), HttpStatus.OK);
        		} catch (IOException ioe) {
                    log.error("Couldn't serialize response for content type application/json", ioe);
                    return new ResponseEntity<CometResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
             } catch (Exception e) {
        			log.error("Accumulo internal error", e);
                return new ResponseEntity<CometResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
        		}
        }

        ResponseEntity<CometResponse> cr = new ResponseEntity<CometResponse>(HttpStatus.BAD_REQUEST);
        return cr;
    }
}

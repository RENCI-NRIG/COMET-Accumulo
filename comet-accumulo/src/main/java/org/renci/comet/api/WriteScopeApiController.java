package org.renci.comet.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;

import org.renci.comet.model.CometResponse;
import org.renci.comet.model.Value;
import org.renci.comet.CometInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.codehaus.jettison.json.JSONObject;
import org.renci.comet.CometOps;
import org.renci.comet.accumuloops.*;
import org.renci.comet.certutils.*;


@javax.annotation.Generated(value = "org.renci.comet.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

@Controller
public class WriteScopeApiController implements WriteScopeApi {

    private static final Logger log = LoggerFactory.getLogger(WriteScopeApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private boolean certValid = false;
    
    private String checkCert = org.renci.comet.CometOps.readProperties()[1];
    
    //@Value("${cometconfig.certCheck}")
    //private String certCheck;

    @org.springframework.beans.factory.annotation.Autowired
    public WriteScopeApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<CometResponse> writeScopePost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String value,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "contextID", required = true) String contextID,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "family", required = true) String family,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Key", required = true) String key,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "readToken", required = true) String readToken,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "writeToken", required = true) String writeToken) {
        if (checkCert.equals("false"))
        		certValid = true;
    	
    		String accept = request.getHeader("Accept");

        X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
        log.debug("WriteScope: got request");
		if (certs == null) {
			log.error("Spring: Certificate is null!");
		} else {
			log.debug("Got client certificate:");
			for (X509Certificate x : certs) {
				log.debug(x.toString());
			}
		}
		
		JSONObject output = new JSONObject();
		CometOps cometOps = new CometOps();
		
		// Check if scope exists, if no such scope, create a new scope which does not require valid certificate.
		try {
			output = cometOps.readScope(contextID, family, key, readToken);
			if (output.length() != 0) {
				log.debug("Scope: " + contextID + " exists, modifying existing Accumulo entry. Valid certificate required");
				certValid = true;
			} else {
				log.debug("Scope: " + contextID + " does not exists, creating new Accumulo entry. Trusted certificate required");
			}
		} catch (AccumuloException | AccumuloSecurityException | TableNotFoundException | TableExistsException e1) {
			log.error("Accumulo internal error", e1);
			return new ResponseEntity<CometResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (certs != null) {
			try {
                for (int i = 0; i < certs.length; i++)
                    certs[i].checkValidity();
                	certValid = true;
            } catch (Exception e) {
            		log.error("Unable to validate certificate!");
			}
		}

		log.debug("WriteScope operation: contextID: " + contextID + "\n family: " + family + "\n key: " + key + "\n value: " + value + "\n readToken: " + readToken + "\n writeToken: " + writeToken);

		if (!certValid) {
			try {
				return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"Invalid certificate: Unauthorized client\",  \"value\" : \"{Unauthorized client}\",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.BAD_REQUEST);
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

		if (readToken.equals(writeToken)) {
			try {
				return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"Invalid tokens: read and write token cannot be the same\",  \"value\" : \"invalid tokens\",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.BAD_REQUEST);
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

		if (!CometOps.isTokenStrong(readToken)) {
			try {
				return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"Read token not strong enough\",  \"value\" : \"weak read token\",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.BAD_REQUEST);
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

		if (!CometOps.isTokenStrong(writeToken)) {
			try {
				return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"Write token not strong enough\",  \"value\" : \"weak write token\",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.BAD_REQUEST);
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

		//Test code below, without cert checking
		if (certValid == true && accept != null && accept.contains("application/json")) {

    			log.debug("WriteScope: certificate is valid");
        		try {
                
        			//String accuValue = value.toString();
        			//org.apache.accumulo.core.data.Value val = new org.apache.accumulo.core.data.Value((CharSequence) value);
        			//public JSONObject writeScope (String contextID, String family, String key, Value value, String readToken, String writeToken)
        			//JSONObject test = cometOps.writeScope("id0001", "hero", "name", "bruce wayne", "secretId", "secretWriteId");
        			output = cometOps.writeScope(contextID, family, key, value, readToken, writeToken);
                CometResponse comet = new CometResponse();
                comet.setValue(output.toString());
                comet.setStatus("OK");
                comet.setMessage("message");
                comet.setVersion("0.1");
                //System.out.println(comet.toString());
                String crTemp = "{  \"message\" : \"success\",  \"value\" : " + output.toString() + ",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"OK\"}";
                return new ResponseEntity<CometResponse>(objectMapper.readValue(crTemp, CometResponse.class), HttpStatus.OK);
        		} catch (IOException ioe) {
                    log.error("Couldn't serialize response for content type application/json", ioe);
                    return new ResponseEntity<CometResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
             } catch (Exception e) {
        			log.error("Accumulo internal error", e);
                return new ResponseEntity<CometResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
        		}

	    }

        return new ResponseEntity<CometResponse>(HttpStatus.BAD_REQUEST);
    }

}

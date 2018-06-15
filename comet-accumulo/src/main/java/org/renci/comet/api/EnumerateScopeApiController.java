package org.renci.comet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;

import org.codehaus.jettison.json.JSONObject;
import org.renci.comet.CometInitializer;
import org.renci.comet.CometOps;
import org.renci.comet.model.CometResponse;
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
@javax.annotation.Generated(value = "org.renci.comet.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

@Controller
public class EnumerateScopeApiController implements EnumerateScopeApi {

    private static final Logger log = LoggerFactory.getLogger(EnumerateScopeApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private boolean certValid = false;

    @org.springframework.beans.factory.annotation.Autowired
    public EnumerateScopeApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<CometResponse> enumerateScopeGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "contextID", required = true) String contextID,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "readToken", required = true) String readToken,@ApiParam(value = "") @Valid @RequestParam(value = "family", required = false) String family) {
        String accept = request.getHeader("Accept");

        X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
		if (certs == null) {
			//System.out.print("Enumerate scope, Cert is NULL!!!\n");
		} else {
			log.debug("Got client certificate:");
			for (X509Certificate x : certs) {
				log.debug(x.toString());
			}
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

		if (accept != null && accept.contains("application/json")) {
            if (accept != null && accept.contains("application/json")) {
                //if (certValid) {
                // For testing only
                try {
                    CometOps cometOps = new CometOps();
                    if (family == null) {

                    } else{
                        return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"family should be empty for enumerate scope operation\",  \"value\" : \"family should be empyty \",  \"version\" : \"" + CometInitializer.COMET_VERSION + "\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.BAD_REQUEST);
                    }
                    //System.out.println("contextID: " + contextID + "readToken: " + readToken);
                    JSONObject output = cometOps.enumerateScopes(contextID, readToken);

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
        }

        ResponseEntity<CometResponse> cr = new ResponseEntity<CometResponse>(HttpStatus.BAD_REQUEST);
        return cr;
    }

}

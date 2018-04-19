package org.renci.comet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;

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
import java.util.List;
@javax.annotation.Generated(value = "org.renci.comet.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

@Controller
public class EnumerateScopeApiController implements EnumerateScopeApi {

    private static final Logger log = LoggerFactory.getLogger(EnumerateScopeApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public EnumerateScopeApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<CometResponse> enumerateScopeGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "contextID", required = true) String contextID,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "family", required = true) String family,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "readToken", required = true) String readToken) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CometResponse>(objectMapper.readValue("{  \"message\" : \"message\",  \"value\" : \"{}\",  \"version\" : \"version\",  \"status\" : \"status\"}", CometResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<CometResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CometResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}

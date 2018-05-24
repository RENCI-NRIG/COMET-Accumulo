/**
 * NOTE: This class is auto generated by the swagger code generator program (1.0.12-1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.renci.comet.api;

import io.swagger.annotations.*;

import org.renci.comet.model.CometResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
@javax.annotation.Generated(value = "org.renci.comet.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

@Api(value = "enumerateScope", description = "the enumerateScope API")
public interface EnumerateScopeApi {

    @ApiOperation(value = "Retrieve a list of existing scopes within a given context.   ", nickname = "enumerateScopeGet", notes = "Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] ", response = CometResponse.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CometResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = CometResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = CometResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = CometResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = CometResponse.class),
        @ApiResponse(code = 200, message = "Unexpected Error", response = CometResponse.class) })
    @RequestMapping(value = "/enumerateScope",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<CometResponse> enumerateScopeGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "contextID", required = true) String contextID,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "readToken", required = true) String readToken,@ApiParam(value = "") @Valid @RequestParam(value = "family", required = false) String family);

}
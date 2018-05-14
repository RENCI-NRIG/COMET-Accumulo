/**
 * NOTE: This class is auto generated by the swagger code generator program (1.0.12-1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.renci.comet.api;

import io.swagger.annotations.*;

import org.renci.comet.model.CometResponse;
import org.renci.comet.model.Value;
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

@Api(value = "writeScope", description = "the writeScope API")
public interface WriteScopeApi {

    @ApiOperation(value = "Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): ", nickname = "writeScopePost", notes = "Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value ", response = CometResponse.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CometResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = CometResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = CometResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = CometResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = CometResponse.class),
        @ApiResponse(code = 200, message = "Unexpected Error", response = CometResponse.class) })
    @RequestMapping(value = "/writeScope",
        produces = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<CometResponse> writeScopePost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String value,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "contextID", required = true) String contextID,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "family", required = true) String family,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Key", required = true) String key,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "readToken", required = true) String readToken,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "writeToken", required = true) String writeToken);

}

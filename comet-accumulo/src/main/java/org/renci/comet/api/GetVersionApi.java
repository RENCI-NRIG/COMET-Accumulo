/**
 * NOTE: This class is auto generated by the swagger code generator program (1.0.12-1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.renci.comet.api;

import org.renci.comet.model.CometResponse;
import io.swagger.annotations.*;
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
@javax.annotation.Generated(value = "org.renci.comet.codegen.languages.SpringCodegen", date = "2018-05-16T16:05:56.629Z")

@Api(value = "getVersion", description = "the getVersion API")
public interface GetVersionApi {

    @ApiOperation(value = "Retrieve the current Comet version and Comet API version. ", nickname = "getVersionGet", notes = "Retrieve the current Comet version and Comet API version. ", response = CometResponse.class, tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = CometResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = CometResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = CometResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = CometResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = CometResponse.class),
        @ApiResponse(code = 200, message = "Unexpected Error", response = CometResponse.class) })
    @RequestMapping(value = "/getVersion",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<CometResponse> getVersionGet();

}

package org.renci.api;

@javax.annotation.Generated(value = "org.renci.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}

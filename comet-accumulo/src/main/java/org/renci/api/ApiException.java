package org.renci.api;

@javax.annotation.Generated(value = "org.renci.codegen.languages.SpringCodegen", date = "2018-04-18T14:21:33.714Z")

public class ApiException extends Exception{
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}

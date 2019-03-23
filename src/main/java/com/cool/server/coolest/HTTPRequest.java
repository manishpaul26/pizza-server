package com.cool.server.coolest;

import java.util.List;

public class HTTPRequest {

    private final HTTPHeader headers;

    public HTTPRequest(List<String> inputReader){
        this.headers = new HTTPHeader(inputReader);
    }

    public String getRequestPath() {
        return this.headers.getRequestPath();
    }

    public String getMethod() {
        return this.headers.getMethod();
    }
}

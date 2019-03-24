package com.cool.server.coolest;

import java.util.List;
import java.util.StringTokenizer;

public class HTTPHeader {

    private final String method;
    private final String requestPath;
    private final String protocol;
    private final String serverName;
    private final String connection;
    private final Long contentLength;


    /**
     * * Request: GET / HTTP/1.1
     * * * Request: Host: localhost:4444
     * Request: Connection: keep-alive
     * Request: Pragma: no-cache
     * Request: Cache-Control: no-cache
     * Request: Upgrade-Insecure-* Requests: 1
     * Request: User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36
     * Request: Accept: text/html,application/xhtml+xml
    * * Request: Accept-Encoding: gzip, deflate, br
    * Request: Accept-Language: en-US,en;q=0.9
    * Request: Cookie:
     * @param headerList
     */
    public HTTPHeader(List<String> headerList) {
        StringTokenizer tokenizer = new StringTokenizer(headerList.get(0));
        this.method = tokenizer.nextToken();
        this.requestPath = tokenizer.nextToken();
        this.protocol = tokenizer.nextToken();

        tokenizer = new StringTokenizer(headerList.get(1));
        tokenizer.nextToken();
        this.serverName = tokenizer.nextToken();

        tokenizer = new StringTokenizer(headerList.get(2));
        tokenizer.nextToken();
        this.connection = tokenizer.nextToken();

        if (headerList.size() > 4 && headerList.get(4).contains("length")) {
            tokenizer = new StringTokenizer(headerList.get(4));
            tokenizer.nextToken();
            this.contentLength = Long.valueOf(tokenizer.nextToken());
        } else {
            this.contentLength = 0L;
        }

    }

    public HTTPHeader() {
        this.method = "GET";
        this.requestPath = "/";
        this.protocol = "";
        this.serverName = "";
        this.connection = "";
        this.contentLength = 0L;
    }


    public String getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath.isEmpty() || requestPath.equalsIgnoreCase("/") ?  "index.html" : requestPath;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getServerName() {
        return serverName;
    }

    public String getConnection() {
        return connection;
    }

    public Long getContentLength() {
        return contentLength;
    }
}

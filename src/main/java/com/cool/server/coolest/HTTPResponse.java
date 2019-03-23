package com.cool.server.coolest;

import java.io.PrintWriter;
import java.util.Date;

public class HTTPResponse {


    private final PrintWriter out;
    private String contentType;
    private long contentLength;
    private int responseCode;
    private String contentDisposition;

    public HTTPResponse(PrintWriter out) {
        this.out = out;

    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    private void flushHeaders() {
        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java Cool Server");
        out.println("Date: " + new Date());
        out.println("Content-length: " + this.contentLength);

        out.println("Content-type: " + this.contentType);

    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = "";
    }
}

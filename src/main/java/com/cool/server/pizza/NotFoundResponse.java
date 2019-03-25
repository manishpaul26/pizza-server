package com.cool.server.pizza;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class NotFoundResponse extends HTTPResponse {


    public NotFoundResponse(File file) throws IOException {
        super(file);
    }

    @Override
    public void writeHeaders(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Server: Java Cool Server");
        out.println("Date: " + new Date());
        out.println("Content-length: " + getContentLength());
        out.println("Connection: keep-alive");

        out.println("Content-type: " + getContentType());
        out.println();
        out.flush();

    }


    @Override
    public void writeContent(BufferedOutputStream outputStream) throws IOException {
        outputStream.write(getFileData());
        outputStream.flush();
    }
}

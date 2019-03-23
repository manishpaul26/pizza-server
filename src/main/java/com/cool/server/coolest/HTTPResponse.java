package com.cool.server.coolest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;

import static com.cool.server.coolest.ContentTypes.IMAGE_JPEG;
import static com.cool.server.coolest.ContentTypes.IMAGE_PNG;
import static com.cool.server.coolest.ContentTypes.TEXT_HTML;

public class HTTPResponse {


    private String contentType;
    private long contentLength;
    private int responseCode;
    private String contentDisposition;
    private File content;
    private byte[] fileData;

    public HTTPResponse(File file) throws IOException {
        this.contentLength = file.length();
        this.fileData = Files.readAllBytes(file.toPath());
        this.content = file;
        this.setContentType(file.getName());
        this.setContentDisposition("");

        System.out.println(this);

    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    private void setContentType(String fileName) {
        final String name = fileName.toLowerCase();
        if (fileName.endsWith(".html")) {
            this.contentType = TEXT_HTML;
        } else if(fileName.endsWith(".png")) {
            this.contentType = IMAGE_PNG;
        } else if(fileName.endsWith(".jpg") || name.endsWith("jpeg")) {
            this.contentType = IMAGE_JPEG;
        } else if (fileName.endsWith(".mp4")) {
            this.contentType = "";
        } else {
            this.contentType = TEXT_HTML;
        }
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    public void writeHeaders(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java Cool Server");
        out.println("Date: " + new Date());
        out.println("Content-length: " + this.contentLength);

        out.println("Content-type: " + this.contentType);
        out.println();
        out.flush();

    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = "";
    }

    public void writeContent(BufferedOutputStream outputStream) throws IOException {
        outputStream.write(fileData);
        outputStream.flush();
    }

    @Override
    public String toString() {
        return "HTTPResponse{" +
                "contentType='" + contentType + '\'' +
                ", contentLength=" + contentLength +
                ", responseCode=" + responseCode +
                ", contentDisposition='" + contentDisposition + '\'' +
                '}';
    }
}

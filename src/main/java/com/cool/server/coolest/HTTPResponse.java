package com.cool.server.coolest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;

import static com.cool.server.coolest.ContentTypes.*;

public class HTTPResponse {


    private String contentType;
    private long contentLength;
    private int responseCode;
    private String contentDisposition;
    private File content;
    private byte[] fileData;

    public HTTPResponse(File file) throws IOException {
        this.contentLength = file.length();
        this.fileData = file.getAbsolutePath().contains("miffy") ? FileCacheService.getCacheService().getCacheMiffy() : Files.readAllBytes(file.toPath());
        //this.fileData = Files.readAllBytes(file.toPath());
        this.content = file;
        this.setContentType(file.getName());
        this.setContentDisposition("");

        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + this);

    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    private void setContentType(String fileName) {
        final String name = fileName.toLowerCase();
        if (name.endsWith(".html")) {
            this.contentType = TEXT_HTML;
        } else if(name.endsWith(".png")) {
            this.contentType = IMAGE_PNG;
        } else if(name.endsWith(".jpg") || name.endsWith("jpeg")) {
            this.contentType = IMAGE_JPEG;
        } else if (name.endsWith(".mp4")) {
            this.contentType = VIDEO_MP4;
        } else if (name.endsWith(".ico")) {
            this.contentType = IMAGE_ICON;
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
        out.println("Connection: keep-alive");

        out.println("Content-type: " + this.contentType);
        out.println();
        out.flush();

    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public void writeContent(BufferedOutputStream outputStream) throws IOException {
        outputStream.write(fileData);
        outputStream.flush();
    }


    public File getContent() {
        return content;
    }

    public byte[] getFileData() {
        return fileData;
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

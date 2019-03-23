package com.cool.server.coolest.methods;

import com.cool.server.coolest.ContentTypes;
import com.cool.server.coolest.HTTPRequest;
import com.cool.server.coolest.HTTPResponse;
import com.cool.server.coolest.exceptions.MissingFileException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class GetMethod implements HTTPMethod {

    private final String CONTENT_PATH = "content/";

    private final String NOT_FOUND = CONTENT_PATH + "errors/404.html";

    private HTTPRequest request;

    private Socket socket;

    public GetMethod(HTTPRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;

    }

    @Override
    public int execute() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {

            System.out.println("I am inside GETTTTT");

            System.out.println("Socket hashcode : " + socket.hashCode());

            final String resourcePath = CONTENT_PATH + request.getRequestPath();

            File file = new File(resourcePath);
            byte[] fileData;
            long length;

            if (!file.exists()) {
                //throw new MissingFileException();

                file = new File(NOT_FOUND);
                length = file.length();
                fileData = Files.readAllBytes(file.toPath());

                out.println("HTTP/1.1 404 Not Found");
                out.println("Server: Java Cool Server");
                out.println("Date: " + new Date());
                out.println("Content-length: " + length);

                out.println("Content-type: " + ContentTypes.TEXT_HTML);

            } else {

                length = file.length();
                fileData = Files.readAllBytes(file.toPath());

                System.out.println("File length: " + length);
                //FileInputStream fileInput = new FileInputStream(file);

                System.out.println("Sending...");

                String contentDisposition = "Content-Disposition: attachment; filename=miffy.png";

                HTTPResponse response = new HTTPResponse(out);
                //esponse.setHeaders();
                response.setContentType(ContentTypes.TEXT_HTML);
                response.setContentLength(length);
                response.setContentDisposition(contentDisposition);

                out.println("HTTP/1.1 200 OK");
                out.println("Server: Java Cool Server");
                out.println("Date: " + new Date());
                out.println("Content-length: " + length);

                out.println("Content-type: " + ContentTypes.TEXT_HTML);
            }

            out.println();
            out.flush();
            outputStream.write(fileData);


            System.out.println("Completed.!");
        } catch(IOException e) {
            e.printStackTrace();

        } finally {
            try {
                System.out.println("Closing socket..");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }
}

package com.cool.server.pizza.methods;

import com.cool.server.pizza.HTTPRequest;
import com.cool.server.pizza.HTTPResponse;
import com.cool.server.pizza.NotFoundResponse;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " I am inside GETTTTT");

            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Socket hashcode : " + socket.hashCode());

            final String resourcePath = CONTENT_PATH + request.getRequestPath();

            File file = new File(resourcePath);

            if (!file.exists()) {
                file = new File(NOT_FOUND);

                HTTPResponse response = new NotFoundResponse(file);

                response.writeHeaders(out);
                response.writeContent(outputStream);

            } else {

                System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Sending...");

                HTTPResponse response = new HTTPResponse(file);
                response.setContentDisposition("");

                response.writeHeaders(out);
                response.writeContent(outputStream);

            }

            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Completed request for : " + resourcePath);
        } catch(IOException e) {
            e.printStackTrace();

        } finally {
/*            try {
                System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Closing socket..");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

        return 0;
    }
}

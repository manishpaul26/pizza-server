package com.cool.server.coolest.methods;

import com.cool.server.coolest.ContentTypes;
import com.cool.server.coolest.CoolestServer;
import com.cool.server.coolest.HTTPRequest;
import com.cool.server.coolest.HTTPResponse;
import com.cool.server.coolest.Servlet;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

public class PostMethod implements HTTPMethod {


    private final HTTPRequest request;
    private final Socket socket;

    public PostMethod(HTTPRequest request, Socket socket) {

        this.request = request;
        this.socket = socket;

    }

    @Override
    public int execute() {

        Class<?> servlet = CoolestServer.servlets.get(request.getRequestPath());
        try {
            Object instance = servlet.newInstance();
            Method get = servlet.getDeclaredMethod("doPost", HTTPRequest.class, HTTPResponse.class);
            get.invoke(instance, request, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        String filePath = "content/new.jpg";
        String filePathPlaceholder = "content/new-#.jpg";
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            int random = new Random().nextInt((10 - 1) + 1) + 1;
            filePath = filePathPlaceholder.replace("#", String.valueOf(random));
        }

        // TODO- revert
        // To test synch
        filePath = "content/new.jpg";

        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(filePath));
             BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
             PrintWriter out = new PrintWriter(socket.getOutputStream())) {

            final String success = "SUCCESS";

            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java Cool Server");
            out.println("Date: " + new Date());
            out.println("Content-length: " + success.getBytes().length);

            out.println("Content-type: " + ContentTypes.TEXT_HTML);
            out.println();
            out.flush();

            outputStream.write(success.getBytes());
            outputStream.flush();


            System.out.println("Size of bytes : " + request.getBytes().length);
            writer.write(request.getBytes());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Writing to post data complete..");
        return 0;
    }


}

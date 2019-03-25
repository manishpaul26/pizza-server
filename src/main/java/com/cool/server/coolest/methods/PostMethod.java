package com.cool.server.coolest.methods;

import com.cool.server.coolest.CommandLineArguments;
import com.cool.server.coolest.ContentTypes;
import com.cool.server.coolest.CoolestServer;
import com.cool.server.coolest.FileIO;
import com.cool.server.coolest.HTTPRequest;
import com.cool.server.coolest.HTTPResponse;
import com.cool.server.coolest.Servlet;
import com.cool.server.coolest.pojo.ContentDisposition;

import java.io.BufferedOutputStream;
import java.io.File;
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

import static com.cool.server.coolest.methods.StatusCodes.SC_INTERNAL_SERVER_ERROR;
import static com.cool.server.coolest.methods.StatusCodes.SC_OK;

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
        ContentDisposition contentDisposition = request.getHeaders().getContentDisposition();
        String requestFileName = contentDisposition != null ? contentDisposition.getName() : filePath;

        CommandLineArguments arguments = CommandLineArguments.getCommandLineArgument(null);
        String fileName =  arguments.isWriteToSameFile() ? filePath : requestFileName;

        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(filePath));
             BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
             PrintWriter out = new PrintWriter(socket.getOutputStream())) {


            boolean status = FileIO.write(fileName, writer, request.getBytes());

            final String statusCode = status ?  "SUCCESS" : "FAILED";

            String statusCodeString = status ? SC_OK : SC_INTERNAL_SERVER_ERROR;

            out.println("HTTP/1.1 " + statusCodeString);
            out.println("Server: Java Cool Server");
            out.println("Date: " + new Date());
            out.println("Content-length: " + statusCode.getBytes().length);

            out.println("Content-type: " + ContentTypes.TEXT_HTML);
            out.println();
            out.flush();

            outputStream.write(statusCode.getBytes());
            outputStream.flush();

            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Size of bytes : " + request.getBytes().length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Writing to post data complete..");
        return 0;
    }


}

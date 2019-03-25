package com.cool.server.pizza.methods;

import com.cool.server.pizza.CommandLineArguments;
import com.cool.server.pizza.ContentTypes;
import com.cool.server.pizza.PizzaServer;
import com.cool.server.pizza.FileIO;
import com.cool.server.pizza.HTTPRequest;
import com.cool.server.pizza.pojo.ContentDisposition;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Date;

import static com.cool.server.pizza.methods.StatusCodes.SC_INTERNAL_SERVER_ERROR;
import static com.cool.server.pizza.methods.StatusCodes.SC_OK;

public class PostMethod implements HTTPMethod {


    private final HTTPRequest request;
    private final Socket socket;

    public PostMethod(HTTPRequest request, Socket socket) {

        this.request = request;
        this.socket = socket;

    }

    @Override
    public int execute() {

        Class<?> servlet = PizzaServer.servlets.get(request.getRequestPath());
        try {
            Object instance = servlet.newInstance();
            Method get = servlet.getDeclaredMethod("doPost", HTTPRequest.class, Socket.class);
            get.invoke(instance, request, socket);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        // Download servlet has handled it.
        if (request.getRequestPath().contains("download")) {
            return 1;
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

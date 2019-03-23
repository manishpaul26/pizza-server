package com.cool.server.coolest.methods;

import com.cool.server.coolest.CoolestServer;
import com.cool.server.coolest.HTTPRequest;
import com.cool.server.coolest.HTTPResponse;
import com.cool.server.coolest.Servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

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
        return 0;
    }


}

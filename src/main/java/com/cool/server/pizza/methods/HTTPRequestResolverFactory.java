package com.cool.server.pizza.methods;

import com.cool.server.pizza.HTTPRequest;
import com.cool.server.pizza.exceptions.MethodNotSupportedException;

import java.net.Socket;

public class HTTPRequestResolverFactory {


    public static HTTPMethod resolveMethod(HTTPRequest request, Socket socket) throws MethodNotSupportedException {

        if (request.getMethod().equalsIgnoreCase("GET")) {
            return new GetMethod(request, socket);
        } else if(request.getMethod().equalsIgnoreCase("POST")) {
            return new PostMethod(request, socket);
        } else {
            throw new MethodNotSupportedException();
        }
    }


}

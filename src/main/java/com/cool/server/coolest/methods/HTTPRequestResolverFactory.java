package com.cool.server.coolest.methods;

import com.cool.server.coolest.HTTPRequest;
import com.cool.server.coolest.exceptions.MethodNotSupportedException;

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

package com.cool.server.coolest;

import com.cool.server.coolest.annotations.ChocoServlet;

@ChocoServlet(path = "/bin/submit")
public class Servlet {

    public void doPost(HTTPRequest request, HTTPResponse response) {
        System.out.println("UNBELIEVABLE!!!" + request.getMethod());
    }

}

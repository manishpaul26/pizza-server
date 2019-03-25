package com.cool.server.coolest;

import com.cool.server.coolest.annotations.ChocoServlet;

import java.net.Socket;

@ChocoServlet(path = "/bin/submit")
public class Servlet {

    public void doPost(HTTPRequest request, Socket socket) {
        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " POST Servlet");
    }

}

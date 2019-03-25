package com.cool.server.pizza;

import com.cool.server.pizza.annotations.ChocoServlet;

import java.net.Socket;

@ChocoServlet(path = "/bin/submit")
public class Servlet {

    public void doPost(HTTPRequest request, Socket socket) {
        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " POST Servlet");
    }

}

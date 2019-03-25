package com.cool.server.coolest;

import com.cool.server.coolest.annotations.ChocoServlet;

@ChocoServlet(path = "/download")
public class DownloadServlet {

    public void doPost(HTTPRequest request, HTTPResponse response) {
        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Downloas Servlet");
    }
}

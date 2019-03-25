package com.cool.server.pizza;

import com.cool.server.pizza.annotations.ChocoServlet;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import static com.cool.server.pizza.ContentTypes.IMAGE_JPEG;

@ChocoServlet(path = "/download")
public class DownloadServlet {

    public void doPost(HTTPRequest request, Socket socket) {

        String filePath = "content/images/northern-lights.jpg";

        try (
             BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             InputStream inputStream = new FileInputStream(filePath)) {


            byte[] allBytes = new byte[inputStream.available()];
            inputStream.read(allBytes);


            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java Cool Server");
            out.println("Date: " + new Date());
            out.println("Content-length: " + allBytes.length);
            out.println("Connection: keep-alive");

            out.println("Content-type: " + IMAGE_JPEG);
            out.println("Content-Disposition: attachment; filename=northern-lights.jpg");
            out.println();
            out.flush();

            outputStream.write(allBytes);
            outputStream.flush();
            inputStream.reset();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Download complete Servlet");
    }
}

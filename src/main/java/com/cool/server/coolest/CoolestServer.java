package com.cool.server.coolest;

import com.cool.server.coolest.exceptions.MethodNotSupportedException;
import com.cool.server.coolest.methods.HTTPMethod;
import com.cool.server.coolest.methods.HTTPRequestResolverFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoolestServer {

    public static final int PORT = 4444;

    private Socket socket;


    CoolestServer(Socket connection) {
        this.socket = connection;
    }


    public static void main(String args[]) {

        try (ServerSocket socket = new ServerSocket(PORT)) {

            System.out.println("Starting server on port.. " + socket.getLocalPort());


            BufferedReader in;
            PrintWriter out;
            BufferedOutputStream outputStream;

            while (true) {
                System.out.println("Running...");
                CoolestServer server = new CoolestServer(socket.accept());

                System.out.println("Sochet hashcode : " + socket.hashCode());

                server.run();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {


        FileInputStream fileInput = null;
        byte[] fileData = null;
        long length = 0;
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {


            List<String> lines = new ArrayList<>();

            lines.add(in.readLine());
            lines.add(in.readLine());
            lines.add(in.readLine());

            HTTPRequest request = new HTTPRequest(lines);

            HTTPMethod method1 = HTTPRequestResolverFactory.resolveMethod(request, this.socket);
            method1.execute();

        } catch (IOException e) {

            e.printStackTrace();
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (MethodNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("exiting..");

    }


}

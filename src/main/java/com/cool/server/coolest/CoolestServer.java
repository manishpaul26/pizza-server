package com.cool.server.coolest;

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
import java.util.Date;
import java.util.StringTokenizer;

public class CoolestServer {

    public static final int PORT = 4444;

    private Socket connection;


    CoolestServer(Socket connection) {
        this.connection = connection;
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
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream())) {

            String input = in.readLine();
            System.out.println("Input " + input);
            StringTokenizer tokenizer = new StringTokenizer(input);

            String method = tokenizer.nextToken();
            String reuqestPath = tokenizer.nextToken();
            String protocol = tokenizer.nextToken();

            if (method.equals("GET")) {
                System.out.println("Inside GET..");
                File index = new File("content/miffy.png");

                fileData = Files.readAllBytes(index.toPath());
                length = index.length();

                System.out.println("File length: " + length);
                fileInput = new FileInputStream(index);

                outputStream.flush();


                System.out.println("Writing...");
            }

            String response = "Yo yo";

            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java Cool Server");
            out.println("Date: " + new Date());
            out.println("Content-length: " + length);
            //out.println("Content-type: " + "image/png");

            out.println("Content-Type: application/octet-stream");
            out.println("Content-Disposition: attachment; filename=miffy.png");
            out.println();

            out.flush();
            outputStream.write(fileData);

            System.out.println("Completed.!");

        } catch (IOException e) {

            e.printStackTrace();
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        System.out.println("exiting..");

    }


}

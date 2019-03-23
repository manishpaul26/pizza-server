package com.cool.server.coolest;

import com.cool.server.coolest.annotations.ChocoServlet;
import com.cool.server.coolest.exceptions.MethodNotSupportedException;
import com.cool.server.coolest.methods.HTTPMethod;
import com.cool.server.coolest.methods.HTTPRequestResolverFactory;
import org.reflections.Reflections;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CoolestServer {

    public static final int PORT = 4444;

    private Socket socket;

    public static Map<String, Class<?>> servlets;


    CoolestServer(Socket connection) {
        this.socket = connection;
        servlets = loadServlets();
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

                System.out.println("Socket hashcode : " + socket.hashCode());

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

            byte [] inputBytes = new byte[10000];
            socket.getInputStream().read(inputBytes);
            
            StringBuffer buffer = new StringBuffer();

            boolean newLine = false;
            int endOfHeaders = 0;

            for (int i=0; i<inputBytes.length; i++) {
                char c = (char) inputBytes[i];
                if (c == '\n') {
                    newLine = true;
                    lines.add(buffer.toString());
                } else if (( (int) c == 13 && newLine)) {
                    System.out.println("............. Detected end of headers............ i : " + i);
                    endOfHeaders =  i;
                    break;
                } else {
                    newLine = false;
                    buffer.append(c);
                }
            }

            byte [] binary = Arrays.copyOfRange(inputBytes, endOfHeaders + 2, inputBytes.length);

            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream("content/new.jpg"));
            writer.write(binary);
            writer.flush();
            writer.close();

            System.out.println("Aage badho...............");

            HTTPRequest request = new HTTPRequest(lines);

            HTTPMethod method = HTTPRequestResolverFactory.resolveMethod(request, this.socket);
            method.execute();

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

    private Map<String, Class<?>> loadServlets() {

        Reflections reflections = new Reflections("com.cool.server");

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ChocoServlet.class);
        if (annotated == null || annotated.size() == 0) {
            return new HashMap<>(0);
        }

        Map<String, Class<?>> servlets = new HashMap<>(annotated.size());

        Iterator<Class<?>> it = annotated.iterator();
        while(it.hasNext()) {
            Class<?> clazz = it.next();
            ChocoServlet annotation = clazz.getDeclaredAnnotation(ChocoServlet.class);
            servlets.put(annotation.path(), clazz);

        }
        return servlets;

    }

}

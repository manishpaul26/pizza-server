package com.cool.server.coolest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPRequest {

    private final HTTPHeader headers;
    private byte[] bytes;

    public HTTPRequest(Socket socket) throws IOException {

        List<String> headerInput = new ArrayList<>();

        byte [] inputBytes = new byte[100];

        InputStream inputStream = socket.getInputStream();
        int length = 100, bytesLeft = inputStream.available();

        StringBuffer buffer = new StringBuffer();
        int endOfHeaders = 0;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        while(bytesLeft > 0) {
            inputStream.read(inputBytes, 0, length);

            for (int i=0; i<inputBytes.length; i++) {
                char c = (char) inputBytes[i];
                if (c == '\n') {
                    headerInput.add(buffer.toString());
                } else if (( inputBytes[i] == -1)) {
                    System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " ............. Detected end of headers............ i : " + i);
                    endOfHeaders =  i;
                    //pending bytes
                    arrayOutputStream.write(Arrays.copyOfRange(inputBytes, i, inputBytes.length));
                    break;
                } else {
                    // Valid character. Add it to the string buffer.
                    buffer.append(c);
                }
            }

            if (endOfHeaders > 0) {
                // break because headers over. Now copy the entire pending array to another array.
                break;
            }

            bytesLeft = inputStream.available() < length ? inputStream.available() : length;
        }

        int index = 0;
        while(inputStream.available() > 0) {
            byte b = (byte) inputStream.read();
            arrayOutputStream.write(b);
            index++;
        }

        this.bytes = arrayOutputStream.toByteArray();

        // Disabling write
        if (false) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " ********** Headers start **********");
            headerInput.forEach(s -> System.out.println("\t" + s));
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " ********** Headers end **********");
        }

        if (headerInput.isEmpty()) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Naughty Keep alive detected!!");
            this.headers = new HTTPHeader();
        } else {
            this.headers = new HTTPHeader(headerInput);
        }

    }

    public String getRequestPath() {
        return this.headers.getRequestPath();
    }

    public String getMethod() {
        return this.headers.getMethod();
    }

    public byte[] getBytes() {
        return bytes;
    }
}

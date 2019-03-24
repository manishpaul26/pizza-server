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
        int endOfHeaders = 0, currentIndex;
        byte[] pendingBytes = new byte[100]; // because headers could end anywhere and then the bytes already read will have to be saved.

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        while(bytesLeft > 0) {
            inputStream.read(inputBytes, 0, length);

            for (int i=0; i<inputBytes.length; i++) {
                char c = (char) inputBytes[i];
                System.out.println((char) inputBytes[i] + " Reading Byte code : " + inputBytes[i]);
                if (c == '\n') {
                    headerInput.add(buffer.toString());
                } else if (( inputBytes[i] == -1)) {
                    System.out.println("............. Detected end of headers............ i : " + i);
                    endOfHeaders =  i;
                    System.out.println("Last detected bytes " + inputBytes[i]);
                    pendingBytes = Arrays.copyOfRange(inputBytes, i, inputBytes.length);
                    arrayOutputStream.write(pendingBytes);
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

        System.out.println("Current available bytes : " + inputStream.available());



        int index = 0;
        while(inputStream.available() > 0) {
            byte b = (byte) inputStream.read();
            arrayOutputStream.write(b);
            index++;
        }

        byte[] maybe = arrayOutputStream.toByteArray();

        System.out.println("Saved " + index + "bytes.");
        System.out.println("Maybe this size is " + maybe.length);


        File f = new File("content/IMG_0030.jpg");

//2592338
        this.bytes = arrayOutputStream.toByteArray();
//        this.bytes = new byte[pendingBytes.length + arrayOutputStream.toByteArray().length];
  //      this.bytes = Arrays.copyOfRange(pendingBytes, 0, pendingBytes.length);
    //    this.bytes = Arrays.copyOfRange(arrayOutputStream.toByteArray(), pendingBytes.length + 1, arrayOutputStream.toByteArray().length);

        System.out.println("Corrected new size : " + this.bytes.length);
        System.out.println("Correct size should be : 2592338");

        System.out.println("File exists? : + " + f.exists() + " FIle lengt!!! " + f.length());

        headerInput.forEach(System.out::println);


        for (int i=0; i< pendingBytes.length; i++) {
            //System.out.println((char) pendingBytes[i] + " Pending Byte code : " + this.bytes[i]);
        }

        for (int i=0; i< 100; i++) {
            //System.out.println((char) this.bytes[i] + " Byte code : " + this.bytes[i]);
        }

        if (headerInput.isEmpty()) {
            System.out.println("Naughty Keep alive detected!!");
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

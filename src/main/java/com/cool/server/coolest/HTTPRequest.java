package com.cool.server.coolest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPRequest {

    private final HTTPHeader headers;
    private final byte[] bytes;

    public HTTPRequest(Socket socket) throws IOException {

        List<String> headerInput = new ArrayList<>();

        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byte [] inputBytes = new byte[100];


        InputStream inputStream = socket.getInputStream();
        int offset = 0, length = 100, availableBytes = inputStream.available(), bytesLeft = inputStream.available();

        StringBuffer buffer = new StringBuffer();
        boolean newLine = false;
        int endOfHeaders = 0;

        while(bytesLeft > 0) {
            inputStream.read(inputBytes, 0, length);

            for (int i=0; i<inputBytes.length; i++) {
                char c = (char) inputBytes[i];
                System.out.println((char) inputBytes[i] + " Reading Byte code : " + inputBytes[i]);
                if (c == '\n') {
                    newLine = true;
                    headerInput.add(buffer.toString());
                } else if (( inputBytes[i] == -1)) {
                    System.out.println("............. Detected end of headers............ i : " + i);
                    endOfHeaders =  i;
                    break;
                } else {
                    // Valid character. Add it to the string buffer.
                    newLine = false;
                    buffer.append(c);
                }
            }

            if (endOfHeaders > 0) {
                // break because headers over. Now copy the entire pending array to another array.
                break;
            }

            bytesLeft = inputStream.available() < length ? inputStream.available() : length;
            System.out.println("Bytes left : " + bytesLeft);
            System.out.println("Bytes completed : " + (availableBytes - inputStream.available()) + " out of total : " + availableBytes);

        }


        this.bytes = new byte[inputStream.available()];

        //this.bytes = Arrays.copyOfRange(inputBytes, endOfHeaders, inputBytes.length);
        inputStream.read(this.bytes);

        headerInput.forEach(System.out::println);


        for (int i=0; i< this.bytes.length; i++) {
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

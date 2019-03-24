package com.cool.server.coolest;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPRequest {

    private final HTTPHeader headers;
    private final byte[] bytes;

    public HTTPRequest(Socket socket) throws IOException {

        List<String> headerInput = new ArrayList<>();
        byte [] inputBytes = new byte[10000];
        socket.getInputStream().read(inputBytes);

        StringBuffer buffer = new StringBuffer();

        boolean newLine = false;
        int endOfHeaders = 0;

        for (int i=0; i<inputBytes.length; i++) {
            char c = (char) inputBytes[i];
            if (c == '\n') {
                newLine = true;
                headerInput.add(buffer.toString());
            } else if (( (int) c == 13 && newLine)) {
                System.out.println("............. Detected end of headers............ i : " + i);
                endOfHeaders =  i;
                break;
            } else {
                newLine = false;
                buffer.append(c);
            }
        }

        headerInput.forEach(System.out::println);

        this.bytes = Arrays.copyOfRange(inputBytes, endOfHeaders + 2, inputBytes.length);

        if (headerInput.isEmpty()) {
            System.out.println("Naughty Keep alive detected!!");
        }
        this.headers = new HTTPHeader(headerInput);

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

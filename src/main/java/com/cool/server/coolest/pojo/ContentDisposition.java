package com.cool.server.coolest.pojo;

import java.util.StringTokenizer;

public class ContentDisposition {

    private final String type;

    private final String name;

    private final String fileName;

    public ContentDisposition(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        tokenizer.nextToken();
        this.type = tokenizer.nextToken();
        this.name = tokenizer.nextToken();
        this.fileName = tokenizer.nextToken();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }
}

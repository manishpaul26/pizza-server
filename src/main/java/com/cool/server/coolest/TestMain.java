package com.cool.server.coolest;

import com.cool.server.coolest.methods.GetMethod;

import java.lang.annotation.Annotation;

public class TestMain {

    public static void main(String[] args) {
        Annotation[] annotate = Servlet.class.getAnnotations();

        for(int i=0; i< annotate.length; i++) {
            System.out.println(annotate[i].annotationType());
        }
    }
}

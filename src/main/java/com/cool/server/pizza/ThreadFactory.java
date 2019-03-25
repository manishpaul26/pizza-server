package com.cool.server.pizza;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadFactory {

    public static ThreadPoolExecutor getThreadPoolExecutor(CommandLineArguments arguments) {
        BlockingQueue q = new ArrayBlockingQueue(arguments.getQueueSize());

        System.out.println("Initializing thread executor with arguments : " + arguments.toString());
        return new ThreadPoolExecutor(arguments.getPoolSize(), arguments.getMaxPoolSize(), 20, TimeUnit.SECONDS, q);
    }
}

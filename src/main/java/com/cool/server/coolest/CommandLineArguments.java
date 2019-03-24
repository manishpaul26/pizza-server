package com.cool.server.coolest;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class CommandLineArguments {


    private int queue;
    private Integer poolSize;
    private Integer maxPoolSize;

    public CommandLineArguments(String[] args) {
        if (args == null){
            return;
        }

        List<String> arguments = Arrays.asList(args);

        for (String arg : arguments) {

            if (StringUtils.containsIgnoreCase(arg, "maxPool")) {
                this.maxPoolSize = getInteger(arg, 10);
            } else if (StringUtils.containsIgnoreCase(arg,"poolSize")) {
                this.poolSize = getInteger(arg, 5);
            } else if (StringUtils.containsIgnoreCase(arg,"queueSize")) {
                this.queue = getInteger(arg, 100);
            }
        }


    }

    private int getInteger(String arg, int defaultValue) {
        try {
            String[] argsSplit = StringUtils.split(arg, "=");
            if (argsSplit.length < 2) {
                return defaultValue;
            }
            return Integer.valueOf(argsSplit[1]);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getQueueSize() {
        return queue;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    @Override
    public String toString() {
        return "CommandLineArguments{" +
                "queue=" + queue +
                ", poolSize=" + poolSize +
                ", maxPoolSize=" + maxPoolSize +
                '}';
    }
}

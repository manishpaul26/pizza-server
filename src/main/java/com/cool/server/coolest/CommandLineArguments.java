package com.cool.server.coolest;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class CommandLineArguments {


    private static CommandLineArguments instance;

    private int queue;
    private Integer poolSize;
    private Integer maxPoolSize;

    private boolean writeToSameFile;

    public static CommandLineArguments getCommandLineArgument(String[] args) {
        if (instance == null) {
            instance = new CommandLineArguments(args);
        }
       return instance;
    }

    private CommandLineArguments(String[] args) {
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
            } else if (StringUtils.containsIgnoreCase(arg,"writeToSameFile")) {
                this.writeToSameFile = getValue(arg, false);
            }
        }


    }

    private boolean getValue(String arg, boolean defaultValue) {
        String[] argsSplit = StringUtils.split(arg, "=");
        if (argsSplit.length < 2) {
            return defaultValue;
        }
        return argsSplit[1].equals("true") ? true : false;
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

    public boolean isWriteToSameFile() {
        return writeToSameFile;
    }

    @Override
    public String toString() {
        return "CommandLineArguments{" +
                "queue=" + queue +
                ", poolSize=" + poolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", writeToSameFile=" + writeToSameFile +
                '}';
    }
}

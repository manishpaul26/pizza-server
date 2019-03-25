package com.cool.server.pizza;

import org.apache.commons.lang3.StringUtils;

import java.net.SocketOptions;
import java.util.Arrays;
import java.util.List;

public class CommandLineArguments {


    private static CommandLineArguments instance;

    private int queueSize = 200;
    private Integer poolSize;
    private Integer maxPoolSize;

    private boolean writeToSameFile;

    private boolean verbose;

    private int port = 4444;

    private Integer socketTimeOut = SocketOptions.SO_TIMEOUT;

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
                this.queueSize = getInteger(arg, 100);
            } else if (StringUtils.containsIgnoreCase(arg,"writeToSameFile")) {
                this.writeToSameFile = getValue(arg, false);
            } else if (StringUtils.containsIgnoreCase(arg,"socketTimeOut")) {
                this.socketTimeOut = getInteger(arg, SocketOptions.SO_TIMEOUT);
            } else if (StringUtils.containsIgnoreCase(arg,"verbose")) {
                this.verbose = getValue(arg, false);
            } else if (StringUtils.containsIgnoreCase(arg,"port")) {
                this.port = getInteger(arg, 4444);
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
        return queueSize;
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

    public Integer getSocketTimeOut() {
        return socketTimeOut;
    }

    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public String toString() {
        return "CommandLineArguments{" +
                "queueSize=" + queueSize +
                ", poolSize=" + poolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", writeToSameFile=" + writeToSameFile +
                ", verbose=" + verbose +
                ", port=" + port +
                ", socketTimeOut=" + socketTimeOut +
                '}';
    }

    public int getPort() {
        return port;
    }
}

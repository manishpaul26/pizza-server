package com.cool.server.pizza;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FileIO {

    private static Set<String> lockedFiles = ConcurrentHashMap.newKeySet();


    private static boolean addLock(String absoutePath) {

        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Adding lock for : " + absoutePath);
        lockedFiles.add(absoutePath);
        return true;
    }

    private static boolean releaseLock(String absoutePath) {
        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Releasing lock for : " + absoutePath);
        lockedFiles.remove(absoutePath);
        return true;
    }

    private static boolean lockExists(String absoutePath) {
        boolean lockStatus = lockedFiles.contains(absoutePath);
        if (lockStatus) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " THE FILE IS LOCKEDDDD!!!!" + absoutePath);
        }
        return lockedFiles.contains(absoutePath);
    }


    public static boolean write(String filePath, BufferedOutputStream outputStream, byte[] content) {

        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " Writing file..... : " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            writeToFile(outputStream, content, filePath);
            return true;
        }

        if (lockExists(filePath)) {
            try {
                Thread.sleep(100);
                if (lockExists(filePath)){
                    return false;
                } else {
                    return writeToFile(outputStream, content, filePath);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return writeToFile(outputStream, content, filePath);
        }

    }

    private static boolean writeToFile(BufferedOutputStream outputStream, byte[] content, String filePath) {
        addLock(filePath);
        try {
            outputStream.write(content);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        releaseLock(filePath);
        return true;
    }
}

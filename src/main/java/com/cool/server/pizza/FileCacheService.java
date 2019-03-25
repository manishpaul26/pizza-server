package com.cool.server.pizza;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static com.cool.server.pizza.Constants.MIFFY;

public class FileCacheService {

    private static FileCacheService cacheService = new FileCacheService();

    private Map<String, byte[]> cachedFiles = new HashMap<>();

    private FileCacheService() {
    }

    public boolean getCachedFile(String filePath) {
        return true;
    }

    public static FileCacheService getCacheService() {
        return cacheService;
    }


    public byte[] getCacheMiffy() {
        if (cachedFiles.containsKey(MIFFY)) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " MIFFY Image is Cached!");
            return cachedFiles.get(MIFFY);
        } else {
            try {
                System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Caching MIFFFYYYYYYY");
                File file = new File(MIFFY);
                byte[] content = Files.readAllBytes(file.toPath());
                cachedFiles.put(MIFFY, content);
                return content;
            }  catch (IOException e) {
                return null;
            }
        }





    }
}

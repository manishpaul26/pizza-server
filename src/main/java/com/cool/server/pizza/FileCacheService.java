package com.cool.server.pizza;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.cool.server.pizza.Constants.MIFFY;

public class FileCacheService {

    private static FileCacheService cacheService;

    private Map<String, byte[]> cachedMiffy = new HashMap<>();

    private Map<String, byte[]> cachedImages = new ConcurrentHashMap<>();

    static boolean initialized = false;

    static boolean isSimulateCacheRace = false;

    private FileCacheService() {
    }

    /**
     * isSimulateCacheRace is just an additional flag that is set through junit tests.
     * @return
     */
    public static FileCacheService getCacheService() {

        if (cacheService == null) {
            System.out.println("*************Cache service is NULL. ******************");
            if (isSimulateCacheRace) {
                initializeInefficiently();
            } else {
                initializeWithSafety();
            }

        }

        //System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : Returning cache service..!");
        return cacheService;
    }

    /**
     * This method initializes without handling any multi-threaded behaviour.
     */
    private static void initializeInefficiently() {
        System.out.println("Multiple threads will access this. Initializing Cache Service.");
        cacheService = new FileCacheService();
        System.out.println("Initialized cache service (again)");
    }

    /**
     * Synchronized and also checks if the object has been initialized or not. If initialized by a thread once,
     * subsequent threads entering this method simply exit without re-initializing.
     */
    static synchronized void initializeWithSafety() {
        System.out.println("Initializing Cache Service");
        if (!initialized) {
            cacheService = new FileCacheService();
            initialized = true;
            System.out.println("Initialized cache service!");
        } else {
            System.out.println("Cache Service Already Initialized..!");
        }

    }


    public byte[] getCachedImage(File imageFIle) throws InterruptedException {
        if (cachedImages.containsKey(imageFIle.getPath())) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : Returning cached image : " + imageFIle);
            return cachedImages.get(imageFIle.getPath());
        } else {
            try {

                synchronized (imageFIle) {
                    System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Entering synchronized block for " + imageFIle.getPath());
                    if (cachedImages.containsKey(imageFIle.getPath())) {
                        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Image has been cached recently " + imageFIle.getPath());
                        return cachedImages.get(imageFIle.getPath());
                    }
                    byte[] content = Files.readAllBytes(imageFIle.toPath());
                    cachedImages.put(imageFIle.getPath(), content);
                    System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Image has been cached : " + imageFIle.getPath());
                    return cachedImages.get(imageFIle.getPath());
                }

            }  catch (IOException e) {
                return null;
            }
        }
    }

    public byte[] getCachedMiffy() {
        if (cachedMiffy.containsKey(MIFFY)) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + " MIFFY Image is Cached!");
            return cachedMiffy.get(MIFFY);
        } else {
            try {
                System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Caching MIFFFYYYYYYY");
                File file = new File(MIFFY);
                byte[] content = Files.readAllBytes(file.toPath());
                cachedMiffy.put(MIFFY, content);
                return content;
            }  catch (IOException e) {
                return null;
            }
        }
    }

    public int getCacheSize() {
        return this.cachedImages.size();
    }

    /**
     * Only for JUNIT for now.
     */
    public static void setSimulateCacheRace() {
        System.out.println("Running in inefficient mode");
        isSimulateCacheRace = true;
    }
}

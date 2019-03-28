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


    /**
     * First check if the image exists in the cached map. If yes, then just returns the image.
     *
     * If the images is not cached, then it tries to synchronize the read from the disk so that the image is read from
     * the file system only once. After that, it puts the image into the map and returns it.
     * @param imageFIle
     * @return
     * @throws InterruptedException
     */
    public byte[] getCachedImage(File imageFIle) throws InterruptedException {
        String path = imageFIle.getPath().intern();
        if (cachedImages.containsKey(path)) {
            System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : Returning cached image : " + imageFIle);
            return cachedImages.get(path);
        } else {
            try {
                synchronized (path) {
                    System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Entering synchronized block for " + path);
                    if (cachedImages.containsKey(path)) {
                        System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Image has been cached recently " + path);
                        return cachedImages.get(path);
                    }
                    byte[] content = Files.readAllBytes(imageFIle.toPath());
                    cachedImages.put(path, content);
                    System.out.println(Thread.currentThread().getId() + " : " + Thread.currentThread().getName() + " : " + "Image has been cached successfully: " + path);
                    return cachedImages.get(path);
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

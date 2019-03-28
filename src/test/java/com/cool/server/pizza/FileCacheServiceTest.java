package com.cool.server.pizza;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FileCacheServiceTest {


    /**
     * This test sets the simulateCacheRace parameter to true, which means that the object will be initialized without any
     * thread safety. All threads can happily initialize the instance without any synchronization.
     */
    @Test
    public void testInefficientMultipleThreadCacheInitialization() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 100, 0, TimeUnit.SECONDS, new ArrayBlockingQueue(200));

        FileCacheService.setSimulateCacheRace();
        for (int i = 0; i<200; i++) {
            executor.submit(FileCacheService::getCacheService);
        }

    }

    /**
     * In this test, the race to initialize the object is handled using an extra condition. The initialization is inside
     * a synchronized block with an extra check to make sure that the object has not been initialized yet.
     */
    @Test
    public void testCorrectMultipleThreadCacheInitialization() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue(200));

        List<Future<FileCacheService>> futureList = new ArrayList<>(200);
        for (int i = 0; i<200; i++) {
            Future<FileCacheService> future = executor.submit(FileCacheService::getCacheService);
            futureList.add(future);
        }

        for (Future<FileCacheService> future : futureList) {
            try {
                if (future.isDone()) {
                    // image has been loaded correctly
                    assertEquals(32036 , future.get().getCachedMiffy().length);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e);
            }
        }
    }


    @Test
    public void testGetImages() throws InterruptedException, ExecutionException {

        //Just to initialize the service
        FileCacheService.getCacheService();
        Map<String, Integer> uniqueImagesCached = new HashMap<>();
        Map<String, List<Future<byte[]>>> futures = new HashMap<>();

        File imagesRoot = new File("content/images/test");
        File[] images =  imagesRoot.listFiles();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 150, 1, TimeUnit.SECONDS, new ArrayBlockingQueue(500));


        // 500 iterations
        for (int i = 0; i< 500; i++) {

            if (executor.getQueue().size() == 500) {
                Thread.sleep(200);
            }

            int randomIndex = (int) (Math.random() *1000) % images.length;
            String randomImagePath = images[randomIndex].getPath();
            uniqueImagesCached.put(randomImagePath, (int) new File(randomImagePath).length());

            Future<byte[]> future = executor.submit(() ->  FileCacheService.getCacheService().getCachedImage(new File(randomImagePath)));
            Future<byte[]> future2 = executor.submit(() ->  FileCacheService.getCacheService().getCachedImage(new File(randomImagePath)));

            futures.compute(randomImagePath, (s, futures1) -> {
                if (futures1 == null) {
                    futures1 = new ArrayList<>();
                }
                futures1.add(future);
                futures1.add(future2);
                return futures1;
            });
        }

        while(executor.getActiveCount() > 0) {
            // wait for the execution to complete
        }
        assertThat("The final size of the map is the same as the number of images cached: ", FileCacheService.getCacheService().getCacheSize(), is(uniqueImagesCached.size()));

        for (String imagePath : futures.keySet()) {
            List<Future<byte[]>> bytes = futures.get(imagePath);
            Integer expectedLength = uniqueImagesCached.get(imagePath);
            for (Future<byte[]> b : bytes) {
                assertThat("Expected length and retrieved length are the same ", b.get().length, is(expectedLength));
            }

        }


    }
}
